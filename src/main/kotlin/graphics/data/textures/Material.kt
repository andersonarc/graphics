package graphics.data.textures

import launcher.Settings
import org.joml.Vector4f

class Material(
    var ambientColour: Vector4f,
    var diffuseColour: Vector4f,
    var specularColour: Vector4f,
    var texture: Texture?,
    var reflectance: Float
) {
    val isTextured: Boolean
        get() = this.texture != null

    constructor() : this(Settings.DEFAULT_COLOR, Settings.DEFAULT_COLOR, Settings.DEFAULT_COLOR, null, 0f)

    constructor(color: Vector4f, reflectance: Float) : this(color, color, color, null, reflectance)

    constructor(texture: Texture) : this(
        Settings.DEFAULT_COLOR,
        Settings.DEFAULT_COLOR,
        Settings.DEFAULT_COLOR,
        texture,
        0f
    )

    constructor(texture: Texture, reflectance: Float) : this(
        Settings.DEFAULT_COLOR,
        Settings.DEFAULT_COLOR,
        Settings.DEFAULT_COLOR,
        texture,
        reflectance
    )
}