package graphics.data.objects.optimization

import graphics.data.objects.Mesh
import graphics.data.objects.Object
import org.joml.FrustumIntersection
import org.joml.Matrix4f


class FrustumCullingFilter {
    private val projectionViewMatrix = Matrix4f()
    private val frustumIntersection = FrustumIntersection()

    fun updateFrustum(projectionMatrix: Matrix4f, viewMatrix: Matrix4f) {
        projectionViewMatrix.set(projectionMatrix)
        projectionViewMatrix.mul(viewMatrix)
        frustumIntersection.set(projectionViewMatrix)
    }

    private fun insideFrustum(x: Float, y: Float, z: Float, boundingRadius: Float): Boolean {
        return frustumIntersection.testSphere(x, y, z, boundingRadius)
    }

    fun filter(objects: List<Object>) {
        for (obj in objects) {
            for (mesh in obj.meshes) {
                val position = obj.position
                obj.insideFrustum = insideFrustum(position.x, position.y, position.z, obj.scale * mesh.boundingRadius)
            }
        }
    }

    fun filter(meshes: ArrayList<Mesh>) {
        for (mesh in meshes) {
            for (obj in mesh.objects) {
                val position = obj.position
                obj.insideFrustum = insideFrustum(position.x, position.y, position.z, obj.scale * mesh.boundingRadius)
            }
        }
    }
}