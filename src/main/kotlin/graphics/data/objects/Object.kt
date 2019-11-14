package graphics.data.objects

import org.joml.Quaternionf
import org.joml.Vector3f


open class Object(
    val meshes: Array<Mesh>,
    var position: Vector3f = Vector3f(0f, 0f, 0f),
    var scale: Float = 1f,
    var rotation: Quaternionf = Quaternionf()
) {
    private var selected = false
    private var textPos = 0
    var frustumCulling = true
    var insideFrustum = false

    constructor(
        mesh: Mesh,
        position: Vector3f = Vector3f(0f, 0f, 0f),
        scale: Float = 1f,
        rotation: Quaternionf = Quaternionf()
    ) : this(arrayOf(mesh), position, scale, rotation)

    fun cleanup() {
        for (mesh in meshes) {
            mesh.cleanup()
        }
    }
    override fun toString(): String {
        return position.x.toString() + ":" + position.y + ":" + position.z +
                " " + rotation.x + ":" + rotation.y + ":" + rotation.z +
                " " + scale
    }
}