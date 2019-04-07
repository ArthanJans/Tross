package tross.parser

import tross.ast.*
import tross.lexer.*
import javax.swing.plaf.nimbus.State


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

    val precedences = hashMapOf(
            TokenType.EQUALS to Priority.EQUALS,
            TokenType.NOTEQUALS to Priority.EQUALS,
            TokenType.LESSTHAN to Priority.LESSGREATER,
            TokenType.GREATERTHAN to Priority.LESSGREATER,
            TokenType.PLUS to Priority.SUM,
            TokenType.MINUS to Priority.SUM,
            TokenType.DIVIDE to Priority.PRODUCT,
            TokenType.MULTIPLY to Priority.PRODUCT
    )

    var curToken: Token
    var peekToken: Token
    var errors = ArrayList<String>()

    val prefixParseFns = HashMap<TokenType, ()->Expression?>()
    val infixParseFns = HashMap<TokenType, (e:Expression)->Expression?>()

    init{
        this.curToken = this.l.nextToken()
        this.peekToken = this.l.nextToken()
        this.prefixParseFns[TokenType.IDENT] = this::parseIdentifier
        this.prefixParseFns[TokenType.INT] = this::parseIntegerLiteral
        this.prefixParseFns[TokenType.NOT] = this::parsePrefixExpression
        this.prefixParseFns[TokenType.MINUS] = this::parsePrefixExpression
        this.prefixParseFns[TokenType.TRUE] = this::parseBoolean
        this.prefixParseFns[TokenType.FALSE] = this::parseBoolean
        this.prefixParseFns[TokenType.LPAREN] = this::parseGroupedExpression

        this.infixParseFns[TokenType.PLUS] = this::parseInfixExpression
        this.infixParseFns[TokenType.MINUS] = this::parseInfixExpression
        this.infixParseFns[TokenType.DIVIDE] = this::parseInfixExpression
        this.infixParseFns[TokenType.MULTIPLY] = this::parseInfixExpression
        this.infixParseFns[TokenType.EQUALS] = this::parseInfixExpression
        this.infixParseFns[TokenType.NOTEQUALS] = this::parseInfixExpression
        this.infixParseFns[TokenType.LESSTHAN] = this::parseInfixExpression
        this.infixParseFns[TokenType.GREATERTHAN] = this::parseInfixExpression
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
            TokenType.LARROW -> this.parseBlockStatement()
            else -> this.parseExpressionStatement()
        }
    }

    fun noPrefixParseFnError(token: TokenType): Expression? {
        val msg = "no prefix parse function for $token found"
        this.errors.add(msg)
        return null
    }

    fun parseGroupedExpression(): Expression? {
        this.nextToken()
        val exp = this.parseExpression(Priority.LOWEST)
        if (!this.expectPeek(TokenType.RPAREN)) {
            return null
        }
        return exp
    }

    fun parseBlockStatement(): BlockStatement? {
        val token = this.curToken
        val statements = mutableListOf<Statement>()
        this.nextToken()
        while (!this.curTokenIs(TokenType.RARROW) && !this.curTokenIs(TokenType.EOF)) {
            val stmt = this.parseStatement()
            if (stmt != null) {
                statements.add(stmt)
            }
            this.nextToken()
        }
        return BlockStatement(token, statements)
    }

    fun parseRetStatement(): ReturnStatement? {
        val tok = this.curToken
        this.nextToken()
        val value = this.parseExpression(Priority.LOWEST)?: return null
        return ReturnStatement(tok, value)
    }

    fun peekPrecedence(): Priority {
        return precedences[this.peekToken.type]?: Priority.LOWEST
    }

    fun curPrecedence(): Priority {
        return precedences[this.curToken.type]?: Priority.LOWEST
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

    fun parseBoolean(): Expression {
        return BooleanExpression(this.curToken, this.curTokenIs(TokenType.TRUE))
    }

    fun parseExpression(precedence: Priority): Expression? {
        if (this.curTokenIs(tross.lexer.TokenType.NEWLINE)) {
            return null
        }
        val prefix = this.prefixParseFns[this.curToken.type]?: return noPrefixParseFnError(this.curToken.type)
        var leftExpression: Expression = prefix()?: return null

        while (!this.peekTokenIs(TokenType.NEWLINE) && precedence < this.peekPrecedence()) {
            val infix = this.infixParseFns[this.peekToken.type]?: return leftExpression
            this.nextToken()
            leftExpression = infix(leftExpression)?: return null
        }
        return leftExpression
    }

    fun parseIdentifier(): Expression {
        return Identifier(this.curToken, this.curToken.literal)
    }

    fun parseIntegerLiteral(): Expression {
        val value = this.curToken.literal.toInt()
        return IntegerLiteral(this.curToken, value)
    }

    fun parseInfixExpression(left: Expression): Expression? {
        val token = this.curToken
        val operator = this.curToken.literal
        val precedence = this.curPrecedence()
        this.nextToken()
        val right = this.parseExpression(precedence)?: return null

        return InfixExpression(token, left, operator, right)
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