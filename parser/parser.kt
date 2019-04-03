package tross.parser

import tross.ast.*
import tross.lexer.*



class Parser(var l: Lexer) {

    enum class Priority {
        LOWEST(),
        EQUALS(),
        LESSGREATER(),
        SUM(),
        PRODUCT(),
        PREFIX(),
        CALL()
    }

    var curToken: Token
    var peekToken: Token
    var errors = ArrayList<String>()

    val prefixParseFns = HashMap<TokenType, ()->Expression?>()
    val infixParseFns = HashMap<TokenType, (e:Expression)->Expression>()

    init{
        this.curToken = this.l.nextToken()
        this.peekToken = this.l.nextToken()
        this.prefixParseFns[TokenType.IDENT] = this::parseIdentifier
        this.prefixParseFns[TokenType.INT] = this::parseIntegerLiteral
        this.prefixParseFns[TokenType.NOT] = this::parsePrefixExpression
        this.prefixParseFns[TokenType.MINUS] = this::parsePrefixExpression
    }

    fun peekError(t: TokenType) {
        val msg = "expected next token to be $t, got $this.peekToken.type instead"
        println(msg)
        this.errors.add(msg)
    }

    fun nextToken() {
        this.curToken = this.peekToken
        this.peekToken = this.l.nextToken()
    }

    fun parseProgram(): Program {
        val statements = ArrayList<Statement>()
        while (!this.curTokenIs(TokenType.EOF)) {
            val stmt = this.parseStatement()
            if (stmt != null) {
                statements.add(stmt)
            }
            this.nextToken()
        }
        return Program(statements)
    }

    fun parseStatement(): Statement? {
        return when(this.curToken.type) {
            TokenType.VAR -> this.parseVarStatement()
            TokenType.RET -> this.parseRetStatement()
            else -> this.parseExpressionStatement()
        }
    }

    fun noPrefixParseFnError(token: TokenType): Expression? {
        val msg = "no prefix parse function for $token found"
        this.errors.add(msg)
        return null
    }

    fun parseRetStatement(): ReturnStatement? {
        val tok = this.curToken
        this.nextToken()
        val value = this.parseExpression(Priority.LOWEST)?: return null
        return ReturnStatement(tok, value)
    }

    fun parseVarStatement(): VarStatement? {
        val token = this.curToken
        if (!this.expectPeek(TokenType.IDENT)) {
            return null
        }

        val name = Identifier(this.curToken, this.curToken.literal)

        if (! this.expectPeek(TokenType.ASSIGN)) {
            return null
        }

        this.nextToken()

        val value = this.parseExpression(Priority.LOWEST)?: return null
        if (this.curTokenIs(TokenType.NEWLINE)) {
            this.nextToken()
        }
        return VarStatement(token, name, value)
    }

    fun parsePrefixExpression(): Expression? {
        val token = this.curToken
        val operator = this.curToken.literal
        this.nextToken()
        val right = this.parseExpression(Priority.PREFIX)?:return null
        return PrefixExpression(token, operator, right)
    }

    fun parseExpressionStatement(): ExpressionStatement? {
        val tok = this.curToken
        val expression = this.parseExpression(Priority.LOWEST)
        if (this.peekTokenIs(TokenType.NEWLINE)) {

        }
        if (expression == null) {
            return null
        }
        return ExpressionStatement(tok, expression)
    }

    fun parseExpression(precedence: Priority): Expression? {
        if (this.curTokenIs(tross.lexer.TokenType.NEWLINE)) {
            return null
        }
        val prefix = this.prefixParseFns[this.curToken.type]
        return prefix?.invoke()?: noPrefixParseFnError(this.curToken.type)
    }

    fun parseIdentifier(): Expression {
        return Identifier(this.curToken, this.curToken.literal)
    }

    fun parseIntegerLiteral(): Expression {
        val value = this.curToken.literal.toInt()
        return IntegerLiteral(this.curToken, value)
    }

    fun curTokenIs(t: TokenType): Boolean {
        return this.curToken.type == t
    }

    fun peekTokenIs(t: TokenType): Boolean {
        return this.peekToken.type == t
    }

    fun expectPeek(t: TokenType): Boolean {
        if (this.peekTokenIs(t)) {
            this.nextToken()
            return true
        } else {
            this.peekError(t)
            return false
        }
    }
}