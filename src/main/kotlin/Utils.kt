import java.io.FileInputStream

fun String.getResource(): String {
    return FileInputStream("src\\main\\resources\\$this").reader(Charsets.UTF_8).readText()
}