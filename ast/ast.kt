package tross.ast

import tross.lexer.Token

interface Node {
    fun tokenLiteral() : String
    fun string() : String
}

interface Statement : Node {
    fun statementNode()
}

interface Expression : Node {
    fun expressionNode()
}

class Program(var statements : ArrayList<Statement>) : Node {

    override fun tokenLiteral(): String {
        if (this.statements.size > 0) {
            return this.statements[0].tokenLiteral()
        } else {
            return ""
        }
    }

    override fun string() : String {
        var out = ""
        
        for (s in this.statements) {
            out += s.string()
        }

        return out
    }

}

class LetStatement(var token: Token, var name: Identifier, var value: Expression): Statement {

    override fun statementNode() {

    }

    override fun tokenLiteral () : String {
        return this.token.literal
    }

    override fun string() : String {
        var out = ""

        out += this.tokenLiteral() + " "
        out += this.name.string()
        out += " = "
        out += this.value.string()
        out += "\n"

        return out
    }
}

class ExpressionStatement(var token: Token, var expression: Expression) :Statement {
    override fun statementNode() {

    }

    override fun tokenLiteral() : String {
        return this.token.literal
    }

    override fun string() : String {
        return this.expression.string()
    }
}

class Identifier(var token: Token, var value: String) : Expression {

    override fun expressionNode() {

    }

    override fun tokenLiteral () : String {
        return this.token.literal
    }

    override fun string() : String {
        return this.value
    }

}