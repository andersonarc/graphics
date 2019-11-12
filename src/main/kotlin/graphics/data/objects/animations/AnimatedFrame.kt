package graphics.data.objects.animations

import org.joml.Matrix4f

class AnimatedFrame {
    private val identityMatrix = Matrix4f()
    private val jointMatrices = ArrayList<Matrix4f>()

    fun setMatrix(index: Int, jointMatrix: Matrix4f) {
        jointMatrices[index] = jointMatrix
    }
}