# Script.kt

A one-class wrapper around `ProcessBuilder`: run an external command, get back
its exit code and output in a lambda.

```kotlin
Script("echo", "hello") { exitCode, output ->
    println("$exitCode: $output")
}
```

stdout and stderr are merged (`redirectErrorStream(true)`). Without that, a
process that writes a lot to stderr fills its error pipe and blocks forever
while the reader is stuck on stdout.

Nothing is caught: if the command is not on `PATH`, `start()` throws
`IOException` at the call site.

Run the built-in self-check with `kotlinc Script.kt -d out && kotlin -cp out snippets.ScriptKt` (prints `ok`).

## Example — [`Example.kt`](Example.kt)

Detect a RAR archive's version and extract it. `file` reports the format
version in its output, so the version is a regex away:

```kotlin
fun getRARVersion(filename: String): Int? {
    var version: Int? = null
    Script("file", filename) { _, output ->
        version = Regex("""RAR archive data,\s*v(\d+)""")
            .find(output)?.groupValues?.get(1)?.toIntOrNull()
    }
    return version
}
```

Useful because RAR 4 and RAR 5 need different extraction flags. `Example.kt`
adds the `unrar` call for v5, and needs `file`, `unrar` and a `test.rar` in the
working directory to actually run.
