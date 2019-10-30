package graphics.data.objects

import graphics.data.textures.Texture
import org.joml.Vector3f

class Object(val mesh: Mesh) {
    var position = Vector3f(0f, 0f, 0f)
    var scale = 1f
    var rotation = Vector3f(0f, 0f, 0f)

    constructor(mesh: Mesh, texture: Texture) : this(mesh) {
        mesh.texture = texture
    }

    fun setPosition(x: Float, y: Float, z: Float) {
        position.x = x
        position.y = y
        position.z = z
    }

    fun setRotation(x: Float, y: Float, z: Float) {
        rotation.x = x
        rotation.y = y
        rotation.z = z
    }

    fun cleanup() {
        mesh.cleanup()
    }

    override fun toString(): String {
        return "[" + position.x + ":" + position.y + ":" + position.z +
                " " + rotation.x + ":" + rotation.y + ":" + rotation.z +
                " " + scale + "]"
    }
}