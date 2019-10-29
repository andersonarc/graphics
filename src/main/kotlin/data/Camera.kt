package data

import org.joml.Vector3f



class Camera(position: Vector3f, rotation: Vector3f) {
    var position = position
        private set
    var rotation = rotation
        private set

    constructor(): this(Vector3f(), Vector3f())

    fun setPosition(x: Float, y: Float, z: Float) {
        position.x = x
        position.y = y
        position.z = z
    }

    fun move(offsetX: Float, offsetY: Float, offsetZ: Float) {
        if (offsetZ != 0f) {
            position.x += Math.sin(Math.toRadians(rotation.y.toDouble())).toFloat() * -1.0f * offsetZ
            position.z += Math.cos(Math.toRadians(rotation.y.toDouble())).toFloat() * offsetZ
        }
        if (offsetX != 0f) {
            position.x += Math.sin(Math.toRadians((rotation.y - 90).toDouble())).toFloat() * -1.0f * offsetX
            position.z += Math.cos(Math.toRadians((rotation.y - 90).toDouble())).toFloat() * offsetX
        }
        position.y += offsetY
    }

    fun rotate(offsetX: Float, offsetY: Float, offsetZ: Float) {
        rotation.x += offsetX
        rotation.y += offsetY
        rotation.z += offsetZ
    }
}