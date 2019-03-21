package tross.lexer

fun testNextToken() {
    var input = """var five = 5
    var ten = 10
    func add (x, y) > ret x + y <
    var result = add(five, ten)
    !true
    false
    if (5 == 5) >
        5
    < elif (5 != 5) >
        false
    < else >
        true
    <
    6 - 3
    99 / 9
    1 * 8
    5 >> 10
    10 << 5
    5 <= 10
    10 >= 5
    """

    var tests = arrayOf(
        ExpectedToken(TokenType.VAR, "var"),
        ExpectedToken(TokenType.IDENT, "five"),
        ExpectedToken(TokenType.ASSIGN, "="),
        ExpectedToken(TokenType.INT, "5"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.VAR, "var"),
        ExpectedToken(TokenType.IDENT, "ten"),
        ExpectedToken(TokenType.ASSIGN, "="),
        ExpectedToken(TokenType.INT, "10"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.FUNCTION, "func"),
        ExpectedToken(TokenType.IDENT, "add"),
        ExpectedToken(TokenType.LPAREN, "("),
        ExpectedToken(TokenType.IDENT, "x"),
        ExpectedToken(TokenType.COMMA, ","),
        ExpectedToken(TokenType.IDENT, "y"),
        ExpectedToken(TokenType.RPAREN, ")"),
        ExpectedToken(TokenType.LARROW, ">"),
        ExpectedToken(TokenType.RET, "ret"),
        ExpectedToken(TokenType.IDENT, "x"),
        ExpectedToken(TokenType.PLUS, "+"),
        ExpectedToken(TokenType.IDENT, "y"),
        ExpectedToken(TokenType.RARROW, "<"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.VAR, "var"),
        ExpectedToken(TokenType.IDENT, "result"),
        ExpectedToken(TokenType.ASSIGN, "="),
        ExpectedToken(TokenType.IDENT, "add"),
        ExpectedToken(TokenType.LPAREN, "("),
        ExpectedToken(TokenType.IDENT, "five"),
        ExpectedToken(TokenType.COMMA, ","),
        ExpectedToken(TokenType.IDENT, "ten"),
        ExpectedToken(TokenType.RPAREN, ")"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.NOT, "!"),
        ExpectedToken(TokenType.TRUE, "true"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.FALSE, "false"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.IF, "if"),
        ExpectedToken(TokenType.LPAREN, "("),
        ExpectedToken(TokenType.INT, "5"),
        ExpectedToken(TokenType.EQUALS, "=="),
        ExpectedToken(TokenType.INT, "5"),
        ExpectedToken(TokenType.RPAREN, ")"),
        ExpectedToken(TokenType.LARROW, ">"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.INT, "5"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.RARROW, "<"),
        ExpectedToken(TokenType.ELIF, "elif"),
        ExpectedToken(TokenType.LPAREN, "("),
        ExpectedToken(TokenType.INT, "5"),
        ExpectedToken(TokenType.NOTEQUALS, "!="),
        ExpectedToken(TokenType.INT, "5"),
        ExpectedToken(TokenType.RPAREN, ")"),
        ExpectedToken(TokenType.LARROW, ">"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.FALSE, "false"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.RARROW, "<"),
        ExpectedToken(TokenType.ELSE, "else"),
        ExpectedToken(TokenType.LARROW, ">"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.TRUE, "true"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.RARROW, "<"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.INT, "6"),
        ExpectedToken(TokenType.MINUS, "-"),
        ExpectedToken(TokenType.INT, "3"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.INT, "99"),
        ExpectedToken(TokenType.DIVIDE, "/"),
        ExpectedToken(TokenType.INT, "9"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.INT, "1"),
        ExpectedToken(TokenType.MULTIPLY, "*"),
        ExpectedToken(TokenType.INT, "8"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.INT, "5"),
        ExpectedToken(TokenType.GREATERTHAN, ">>"),
        ExpectedToken(TokenType.INT, "10"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.INT, "10"),
        ExpectedToken(TokenType.LESSTHAN, "<<"),
        ExpectedToken(TokenType.INT, "5"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.INT, "5"),
        ExpectedToken(TokenType.LTEQUAL, "<="),
        ExpectedToken(TokenType.INT, "10"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.INT, "10"),
        ExpectedToken(TokenType.GTEQUAL, ">="),
        ExpectedToken(TokenType.INT, "5"),
        ExpectedToken(TokenType.NEWLINE, "\n"),
        ExpectedToken(TokenType.EOF, "")
    )
    var l = Lexer(input)

    for ((index, expectedToken) in tests.withIndex()) {
        var tok = l.nextToken()

        if (tok.type != expectedToken.type) {
            println(String.format("tests[%d] - tokentype wrong. expected=%s, got=%s", index, expectedToken.type, tok.type))
        }

        if (tok.literal != expectedToken.literal) {
            println(String.format("tests[%d] - literal wrong. expected=%s, got=%s", index, expectedToken.literal, tok.literal))
        }
    }
}

class ExpectedToken(val type: TokenType, val literal: String) {

}

