package tross.parser

import tross.ast.*
import tross.lexer.*

class Parser(var l: Lexer) {
    var curToken: Token
    var peekToken: Token
    var errors = ArrayList<String>()

    init{
        this.curToken = this.l.nextToken()
        this.peekToken = this.l.nextToken()
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

    fun parseProgram(): Program? {
        val statements = ArrayList<Statement>()
        while (!this.curTokenIs(TokenType.EOF)) {
            var stmt = this.parseStatement()
            if (stmt != null) {
                statements.add(stmt)
            }
            this.nextToken()
        }
        return Program(statements)
    }

    fun parseStatement(): Statement? {
        return when(this.curToken.type) {
            // TokenType.VAR -> this.parseLetStatement()
            else -> null
        }
    }

    // fun parseLetStatement(): LetStatement? {
    //     if (!this.expectPeek(TokenType.IDENT)) {
    //         return null
    //     }
        
    //     val name = Identifier(this.curToken, this.curToken.literal)
        
    //     if (! this.expectPeek(TokenType.ASSIGN)) {
    //         return null
    //     }

    //     val value : Expression
    //     //TODO: skipping expressions until we encounter a newline
    //     while (! this.curTokenIs(TokenType.NEWLINE)) {
    //         this.nextToken()
    //     }

    //     return LetStatement(this.curToken, name, value)
    // }

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