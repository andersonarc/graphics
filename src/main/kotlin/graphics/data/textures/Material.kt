package graphics.data.textures

import launcher.Settings
import org.joml.Vector4f

class Material(
    var ambientColor: Vector4f,
    var diffuseColor: Vector4f,
    var specularColor: Vector4f,
    var texture: Texture?,
    var reflectance: Float
) {

    val isTextured: Boolean
        get() = this.texture != null

    constructor(ambientColor: Vector4f, diffuseColor: Vector4f, specularColor: Vector4f, reflectance: Float) :
            this(ambientColor, diffuseColor, specularColor, null, reflectance)

    constructor() : this(Settings.DEFAULT_COLOR, Settings.DEFAULT_COLOR, Settings.DEFAULT_COLOR, null, 0f)

    fun cleanup() {
        texture?.cleanup()
    }
}