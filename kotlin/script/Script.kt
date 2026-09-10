package snippets

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Runs an external command and hands the caller its exit code and combined output.
 *
 *   Script("file", "./archive.rar") { exitCode, output -> println(output) }
 *
 * stderr is folded into stdout (redirectErrorStream) so a chatty process cannot
 * fill its error pipe and deadlock while we are blocked reading stdout.
 */
class Script(
    vararg command: String,
    whenDone: (Int, String) -> Unit
) {
    init {
        val process = ProcessBuilder(command.toList())
            .redirectErrorStream(true)
            .start()
        val output = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }
        whenDone(process.waitFor(), output)
    }
}

fun main() {
    Script("echo", "hello") { exitCode, output ->
        check(exitCode == 0) { "expected exit 0, got $exitCode" }
        check(output.trim() == "hello") { "expected 'hello', got '${output.trim()}'" }
    }
    println("ok")
}
