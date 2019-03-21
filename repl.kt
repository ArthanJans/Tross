package tross.repl

import tross.lexer.*

val PROMPT = ">> "

fun start() {
    while (true) {
        print(PROMPT)
        var line = readLine()
        if (line != null) {
            var l = Lexer(line)
            var tok = l.nextToken()
            while (tok.type != TokenType.EOF) {
                println(tok.type.toString() + " : " + tok.literal)
                tok = l.nextToken()
            }
        }
    }
}