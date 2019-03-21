package tross.lexer

class Token(val type: TokenType, val literal: String) {
    
}

enum class TokenType(val tokenType: String) {
    ILLEGAL("ILLEGAL"),
    EOF("EOF"),
    
    // Identifiers + literals
    IDENT("IDENT"), // add, foobar, x, y, ...
    INT("INT"), // 1232345

    // Operators
    ASSIGN("="),
    PLUS("+"),
    MINUS("-"),
    NOT("!"),
    MULTIPLY("*"),
    DIVIDE("/"),
    LESSTHAN("<<"),
    GREATERTHAN(">>"),
    LTEQUAL("<="),
    GTEQUAL(">="),
    EQUALS("=="),
    NOTEQUALS("!="),

    // Delimiters
    COMMA(","),
    LPAREN("("),
    RPAREN(")"),
    LARROW(">"),
    RARROW("<"),
    NEWLINE("\n"),

    // Keywords
    FUNCTION("FUNC"),
    VAR("VAR"),
    RET("RET"),
    FALSE("FALSE"),
    TRUE("TRUE"),
    IF("IF"),
    ELSE("ELSE"),
    ELIF("ELIF")

}

var keywords = hashMapOf(
    "func" to TokenType.FUNCTION,
    "var" to TokenType.VAR,
    "ret" to TokenType.RET,
    "true" to TokenType.TRUE,
    "false" to TokenType.FALSE,
    "if" to TokenType.IF,
    "elif" to TokenType.ELIF,
    "else" to TokenType.ELSE
)

fun lookupIdent(ident: String): TokenType{
    var out = keywords[ident]
    if (out != null) {
        return out
    } else {
        return TokenType.IDENT
    }
}