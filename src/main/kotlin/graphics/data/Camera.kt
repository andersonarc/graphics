package graphics.data

import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin


class Camera(position: Vector3f, rotation: Vector3f) {
    var position = position
        private set
    var rotation = rotation
        private set

    constructor(): this(Vector3f(), Vector3f())

    fun move(offsetX: Float, offsetY: Float, offsetZ: Float) {
        if (offsetZ != 0f) {
            position.x += sin(Math.toRadians(rotation.y.toDouble())).toFloat() * -1.0f * offsetZ
            position.z += cos(Math.toRadians(rotation.y.toDouble())).toFloat() * offsetZ
        }
        if (offsetX != 0f) {
            position.x += sin(Math.toRadians((rotation.y - 90).toDouble())).toFloat() * -1.0f * offsetX
            position.z += cos(Math.toRadians((rotation.y - 90).toDouble())).toFloat() * offsetX
        }
        position.y += offsetY
    }

    fun rotate(offsetX: Float, offsetY: Float, offsetZ: Float) {
        rotation.x += offsetX
        rotation.y += offsetY
        rotation.z += offsetZ
    }
}