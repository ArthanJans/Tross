package tross.ast

import tross.lexer.Token

interface Node {
    fun tokenLiteral() : String
    fun string() : String
}

interface Statement : Node

interface Expression : Node

class Program(var statements : MutableList<Statement>) : Node {

    override fun tokenLiteral(): String {
        return if (this.statements.size > 0) {
            this.statements[0].tokenLiteral()
        } else {
            ""
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

class BlockStatement(val token: Token, val statements: MutableList<Statement>): Statement {
    override fun tokenLiteral(): String {
        return this.token.literal
    }

    override fun string(): String {
        var out = ""
        for (s in statements) {
            out += s.string()
        }
        return out
    }
}

class ReturnStatement(var token: Token, var returnValue: Expression): Statement {

    override fun tokenLiteral(): String {
        return this.token.literal
    }

    override fun string(): String {
        var out = ""

        out += this.tokenLiteral() + " "
        out += this.returnValue.string()
        out += "\n"

        return out
    }

}

class VarStatement(var token: Token, var name: Identifier, var value: Expression): Statement {

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

    override fun tokenLiteral() : String {
        return this.token.literal
    }

    override fun string() : String {
        return this.expression.string()
    }
}

class InfixExpression(var token: Token, var left: Expression, var operator: String, var right: Expression): Expression {

    override fun tokenLiteral(): String {
        return token.literal
    }

    override fun string(): String {
        return "(${left.string()} $operator ${right.string()})"
    }
}

class BooleanExpression(var token: Token, var value: Boolean): Expression {
    override fun tokenLiteral(): String {
        return this.token.literal
    }

    override fun string(): String {
        return this.token.literal
    }
}

class IntegerLiteral(var token: Token, var value: Int) : Expression {

    override fun tokenLiteral(): String {
        return this.token.literal
    }

    override fun string(): String {
        return this.token.literal
    }
}

class PrefixExpression(val token: Token, val operator: String, val right: Expression): Expression {

    override fun tokenLiteral(): String {
        return this.token.literal
    }

    override fun string(): String {
        var out = ""

        out += "("
        out += this.operator
        out += this.right.string()
        out += ")"
        return out
    }
}

class Identifier(var token: Token, var value: String) : Expression {

    override fun tokenLiteral () : String {
        return this.token.literal
    }

    override fun string() : String {
        return this.value
    }

}