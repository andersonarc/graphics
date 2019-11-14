package graphics.data.objects.loaders

import graphics.data.objects.Mesh
import graphics.data.objects.animations.AnimatedObject
import launcher.Settings
import launcher.Settings.SHADER_PATH
import java.io.FileInputStream

fun String.shader(): String {
    return FileInputStream(SHADER_PATH + this).reader(Charsets.UTF_8).readText()
}

fun String.staticMesh(): Array<Mesh> {
    return MeshCache.staticMesh("${Settings.MODEL_PATH}\\$this\\$this.obj", "${Settings.MODEL_PATH}\\$this")
}

fun String.animatedObject(): AnimatedObject {
    return loadAnimatedObject("${Settings.MODEL_PATH}\\$this\\$this.md5anim", "${Settings.MODEL_PATH}\\$this")
}