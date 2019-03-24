package tross.ast

import tross.lexer.*

fun testString() {
    val program = Program(
        mutableListOf(
            VarStatement(
                Token(TokenType.VAR, "var"),
                Identifier(
                        Token(TokenType.IDENT, "myVar"),
                        "myVar"
                ),
                Identifier(
                        Token(TokenType.IDENT, "anotherVar"),
                        "anotherVar"
                )
            ) as Statement
        )
    )

    if (program.string() != "var myVar = anotherVar\n") {
        println("program.string() wrong. got=${program.string()}")
    }
}