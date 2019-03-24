package tross

fun main(args: Array<String>) {
    tross.lexer.testNextToken()
    tross.ast.testString()
    tross.parser.testLetStatements()
    tross.parser.testIdentifierExpression()
    tross.parser.testIntegerLiteralExpression()
    println("Hello! This is the Tross programming language!")
    tross.repl.start()
}