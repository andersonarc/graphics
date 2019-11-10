package graphics.misc

import launcher.Settings.SHADER_PATH
import java.io.FileInputStream

fun String.shader(): String {
    return FileInputStream(SHADER_PATH + this).reader(Charsets.UTF_8).readText()
}