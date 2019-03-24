package tross.parser

import tross.ast.*
import tross.lexer.*

fun testLetStatements() {
    val input = """
    var x = 5
    var y = 10
    var foobar = 838383
    """

    val l = Lexer(input)
    val p = Parser(l)

    val program = p.parseProgram()
    checkParseErrors(p)

    if (program.statements.size != 3) {
        println("program.statements does not contain 3 statements. got=" + program.statements.size)
        return
    }

    val tests = arrayOf(
        ExpectedStatement("x"),
        ExpectedStatement("y"),
        ExpectedStatement("foobar")
    )

    for ((index, expectedStatement) in tests.withIndex()) {
        val stmt = program.statements[index]

        if (! testLetStatement(stmt, expectedStatement.expectedIdentifier)) {
            return
        }
    }
}

fun testLetStatement(s: Statement, name: String): Boolean {
    if (s.tokenLiteral() != "var") {
        println("s.TokenLiteral not 'var'. got=" + s.tokenLiteral())
        return false
    }
    if (s !is VarStatement) {
        println("s not LetStatemenet. got=" + s)
        return false
    } else {
        if(s.name.value != name) {
            println("s.name.value not'" + name + "'. got=" + s.name.value)
            return false
        }

        if(s.name.tokenLiteral() != name) {
            println("s.name not'" + name + "'. got=" + s.name)
            return false
        }
        return true
    }
}

fun testIdentifierExpression() {
    val input = "foobar"

    val l = Lexer(input)
    val p = Parser(l)
    val program = p.parseProgram()
    checkParseErrors(p)

    if (program.statements.size != 1) {
        println("program has not enough statements. got=${program.statements.size}")
    }
    val stmt = program.statements[0]
    if (stmt !is ExpressionStatement) {
        println("program.statements[0] is not ast.ExpressionStatement. got=$stmt")
    } else {
        val ident = stmt.expression
        if (ident !is Identifier) {
            println("exp no Identifier. got=$ident")
        } else {
            if (ident.value != "foobar") {
                println("ident.value not foobar. got=${ident.value}")
            }
            if (ident.tokenLiteral() != "foobar") {
                println("ident.tokenLiteral not foobar. got=${ident.tokenLiteral()}")
            }
        }
    }
}

fun testIntegerLiteralExpression() {
    val input = "5"
    val l = Lexer(input)
    val p = Parser(l)
    val program = p.parseProgram()
    checkParseErrors(p)

    if (program.statements.size != 1) {
        println("program has not enough statements. got=${program.statements.size}")
    }
    val stmt = program.statements[0]
    if (stmt !is ExpressionStatement) {
        println("programStatements[0] is not ExpressionStatement. got=$stmt")
    } else {
        val literal = stmt.expression
        if (literal !is IntegerLiteral) {
            println("exp not IntegeLiteral. got=$literal")
        } else {
            if (literal.value != 5) {
                println("literal.value not 5. got=${literal.value}")
            }
            if (literal.tokenLiteral() != "5") {
                println("literal.tokenLiteral not 5. got=${literal.tokenLiteral()}")
            }
        }
    }
}

fun checkParseErrors(p: Parser) {
    val errors = p.errors
    if (errors.size == 0) {
        return
    }

    println("parser has $errors.size errors")
    for (msg in errors) {
        println("parser error: $msg")
    }
    kotlin.system.exitProcess(1)
}

class ExpectedStatement(val expectedIdentifier: String)