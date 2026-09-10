package snippets

import kotlin.io.path.createTempDirectory

/**
 * Real use of [Script]: ask `file` what version a RAR archive is, then shell out
 * to `unrar` for the ones we know how to extract.
 *
 * Needs `file`, `unrar` and a `test.rar` in the working directory to do anything.
 */
fun main() {
    val filename = "test.rar"
    when (getRARVersion(filename)) {
        5 -> println(extractV5RAR(filename))
        4 -> println("RAR 4 — needs the v4 unrar flags")
        else -> println("Unknown RAR version")
    }
}

fun getRARVersion(filename: String): Int? {
    var version: Int? = null
    Script("file", filename) { _, output ->
        version = Regex("""RAR archive data,\s*v(\d+)""")
            .find(output)?.groupValues?.get(1)?.toIntOrNull()
    }
    return version
}

fun extractV5RAR(filename: String): String {
    val destination = createTempDirectory("rar").toFile()
    var output = ""
    Script("unrar", "x", "-v", filename, "$destination") { _, o -> output = o }
    return output
}
