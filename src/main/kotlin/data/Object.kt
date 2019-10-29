package data

import org.joml.Vector3f

class Object(val mesh: Mesh) {
    var position = Vector3f(0f, 0f, 0f)
        private set
    var scale = 1f
    var rotation = Vector3f(0f, 0f, 0f)
        private set

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
}