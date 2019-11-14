package graphics.data.textures

import launcher.Settings.DEFAULT_COLOR
import org.joml.Vector4f

class Material(
    var ambientColor: Vector4f = DEFAULT_COLOR,
    var diffuseColor: Vector4f = DEFAULT_COLOR,
    var specularColor: Vector4f = DEFAULT_COLOR,
    var reflectance: Float = 0f,
    var texture: Texture? = null
) {

    val isTextured: Boolean
        get() = this.texture != null

    fun cleanup() {
        texture?.cleanup()
    }
}