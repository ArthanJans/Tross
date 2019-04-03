package tross

fun main(args: Array<String>) {
    tross.lexer.testNextToken()
    tross.ast.testString()
    tross.parser.testReturnStatement()
    tross.parser.testLetStatements()
    tross.parser.testIdentifierExpression()
    tross.parser.testIntegerLiteralExpression()
    tross.parser.testParsingPrefixExpressions()
    tross.parser.testParsingInfixExpressions()
    tross.parser.testOperatorPrecedenceParsing()
    println("Hello! This is the Tross programming language!")
    tross.repl.start()
}