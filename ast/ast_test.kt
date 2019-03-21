package tross.ast

import tross.lexer.*

fun testString() {
    var array = ArrayList<Statement>()
    array.add(
        LetStatement(
            Token(TokenType.VAR, "var"),
            Identifier(
                Token(TokenType.IDENT, "myVar"),
                "myVar"
            ),
            Identifier(
                Token(TokenType.IDENT, "anotherVar"),
                "anotherVar"
            )
        )
    )
    val program = Program(array)

    if (program.string() != "var myVar = anotherVar\n") {
        println("program.string() wrong. got=${program.string()}")
    }
}