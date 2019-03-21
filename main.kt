package main
import tross.*

fun main(args: Array<String>) {
    tross.lexer.testNextToken()
    tross.ast.testString()
    tross.parser.testLetStatements()
    println("Hello! This is the Tross programming language!")
    tross.repl.start()
}