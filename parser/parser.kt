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

    val prefixParseFns = HashMap<TokenType, ()->Expression>()
    val infixParseFns = HashMap<TokenType, (e:Expression)->Expression>()

    init{
        this.curToken = this.l.nextToken()
        this.peekToken = this.l.nextToken()
        this.prefixParseFns[TokenType.IDENT] = this::parseIdentifier
        this.prefixParseFns[TokenType.INT] = this::parseIntegerLiteral
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
            else -> this.parseExpressionStatement()
//            else -> null
        }
    }

     fun parseVarStatement(): VarStatement? {
         if (!this.expectPeek(TokenType.IDENT)) {
             return null
         }

         val name = Identifier(this.curToken, this.curToken.literal)

         if (! this.expectPeek(TokenType.ASSIGN)) {
             return null
         }

         val value = this.parseExpression(Priority.LOWEST)
         if (this.curTokenIs(TokenType.NEWLINE)) {
             this.nextToken()
         }
         if (value == null) {
             return null
         }
         return VarStatement(this.curToken, name, value)
     }

    fun parseExpressionStatement(): ExpressionStatement? {
        val expression = this.parseExpression(Priority.LOWEST)
        if (this.peekTokenIs(TokenType.NEWLINE)) {

        }
        if (expression == null) {
            return null
        }
        return ExpressionStatement(this.curToken, expression)
    }

    fun parseExpression(precedence: Priority): Expression? {
        val prefix = this.prefixParseFns[this.curToken.type]
        return prefix?.invoke()
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