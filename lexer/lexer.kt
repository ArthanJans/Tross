package tross.lexer

class Lexer(input: String) {

    val input: String

    init {
        if (input[input.length - 1] != '\n') {
            this.input = input + '\n'
        } else {
            this.input = input
        }
    }

    var position = 0
    var readPosition = 0
    var ch = 0.toChar()

    init{
        this.readChar()
    }

    fun readChar() {
        if (this.readPosition >= this.input.length) {
            this.ch = 0.toChar()
        } else {
            this.ch = this.input[this.readPosition]
        }
        this.position = this.readPosition
        this.readPosition++
    }

    fun nextToken(): Token {

        this.skipWhitespace()

        val tok = when(this.ch) {
            '\n' -> newToken(TokenType.NEWLINE, this.ch)
            '=' -> if (this.peekChar() == '=') {
                val ch = this.ch
                this.readChar()
                val literal = ch.toString() + this.ch.toString()
                Token(TokenType.EQUALS, literal)
            } else {
                newToken(TokenType.ASSIGN, this.ch)
            }
            '(' -> newToken(TokenType.LPAREN, this.ch)
            ')' -> newToken(TokenType.RPAREN, this.ch)
            ',' -> newToken(TokenType.COMMA, this.ch)
            '+' -> newToken(TokenType.PLUS, this.ch)
            '>' -> if (this.peekChar() == '>') {
                val ch = this.ch
                this.readChar()
                val literal = ch.toString() + this.ch.toString()
                Token(TokenType.GREATERTHAN, literal)
            } else if (this.peekChar() == '=') {
                val ch = this.ch
                this.readChar()
                val literal = ch.toString() + this.ch.toString()
                Token(TokenType.GTEQUAL, literal)
            } else {
                newToken(TokenType.LARROW, this.ch)
            }
            '<' -> if (this.peekChar() == '<') {
                val ch = this.ch
                this.readChar()
                val literal = ch.toString() + this.ch.toString()
                Token(TokenType.LESSTHAN, literal)
            } else if (this.peekChar() == '=') {
                val ch = this.ch
                this.readChar()
                val literal = ch.toString() + this.ch.toString()
                Token(TokenType.LTEQUAL, literal)
            } else {
                newToken(TokenType.RARROW, this.ch)
            }
            '!' -> if (this.peekChar() == '=') {
                val ch = this.ch
                this.readChar()
                val literal = ch.toString() + this.ch.toString()
                Token(TokenType.NOTEQUALS, literal)
            } else {
                newToken(TokenType.NOT, this.ch)
            }
            '-' -> newToken(TokenType.MINUS, this.ch)
            '*' -> newToken(TokenType.MULTIPLY, this.ch)
            '/' -> newToken(TokenType.DIVIDE, this.ch)
            0.toChar() -> Token(TokenType.EOF, "")
            else -> if (this.ch.isLetter()) {
                val ident = this.readIdentifier()
                return Token(lookupIdent(ident), ident)
            } else if (this.ch.isDigit()) {
                return Token(TokenType.INT, this.readNumber())
            } else {
                newToken(TokenType.ILLEGAL, this.ch)
            }
        }
        this.readChar()
        return tok
    }

    fun newToken(tokenType: TokenType, ch: Char): Token {
        return Token(tokenType, ch.toString())
    }

    fun peekChar(): Char {
        if (this.readPosition >= this.input.length) {
            return 0.toChar()
        } else {
            return this.input[this.readPosition]
        }
    }

    fun readIdentifier(): String {
        val position = this.position
        while (this.ch.isLetter()) {
            this.readChar()
        }
        return this.input.slice(position until this.position)
    }

    fun readNumber(): String {
        val position = this.position
        while (this.ch.isDigit()) {
            this.readChar()
        }
        return this.input.slice(position until this.position)
    }

    fun skipWhitespace() {
        while (this.ch == ' ' || this.ch == '\t' || this.ch == '\r') {
            this.readChar()
        }
    }
}