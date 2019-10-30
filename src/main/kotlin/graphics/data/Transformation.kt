package graphics.data

import graphics.data.objects.Object
import org.joml.Matrix4f
import org.joml.Vector3f

class Transformation {
    private var projectionMatrix = Matrix4f()
    private var modelViewMatrix = Matrix4f()
    private var viewMatrix = Matrix4f()

    fun projectionMatrix(fov: Float, width: Float, height: Float, zNear: Float, zFar: Float): Matrix4f {
        val aspectRatio = width / height
        projectionMatrix.identity()
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar)
        return projectionMatrix
    }

    fun modelViewMatrix(obj: Object, viewMatrix: Matrix4f): Matrix4f {
        val rotation = obj.rotation
        modelViewMatrix.set(viewMatrix).translate(obj.position)
            .rotateX(Math.toRadians((-rotation.x).toDouble()).toFloat())
            .rotateY(Math.toRadians((-rotation.y).toDouble()).toFloat())
            .rotateZ(Math.toRadians((-rotation.z).toDouble()).toFloat()).scale(obj.scale)
        return modelViewMatrix
    }

    fun viewMatrix(camera: Camera): Matrix4f {
        val cameraPos = camera.position
        val rotation = camera.rotation

        viewMatrix.identity()
        viewMatrix.rotate(Math.toRadians(rotation.x.toDouble()).toFloat(), Vector3f(1f, 0f, 0f))
            .rotate(Math.toRadians(rotation.y.toDouble()).toFloat(), Vector3f(0f, 1f, 0f))
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z)
        return viewMatrix
    }
}