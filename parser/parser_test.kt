package tross.parser

import tross.ast.*
import tross.lexer.*

fun testLetStatements() {
    val input = """
    let x = 5
    let y = 10
    let foobar = 838383
    """

    var l = Lexer(input)
    var p = Parser(l)

    var program = p.parseProgram()
    checkParseErrors(p)

    if (program == null) {
        println("ParseProgram() returned null")
        return
    }
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
        var stmt = program.statements[index]

        if (! testLetStatement(stmt, expectedStatement.expectedIdentifier)) {
            return
        }
    }
}

fun testLetStatement(s: Statement, name: String): Boolean {
    if (s.tokenLiteral() != "let") {
        println("s.TokenLiteral not 'let'. got=" + s.tokenLiteral())
        return false
    }
    if (!(s is LetStatement)) {
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

class ExpectedStatement(val expectedIdentifier: String) {

}