package graphics.misc

import java.io.FileInputStream

fun String.resource(path: String): String {
    return FileInputStream("$path$this").reader(Charsets.UTF_8).readText()
}

fun String.fileLines(): Array<String> {
    return FileInputStream(this).reader(Charsets.UTF_8).readLines().toTypedArray()
}