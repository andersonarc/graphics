package graphics.data.objects

import org.joml.Vector3f

class Object(val mesh: Mesh, var position: Vector3f, var scale: Float, var rotation: Vector3f) {
    constructor(mesh: Mesh) : this(mesh, Vector3f(0f, 0f, 0f), 1f, Vector3f(0f, 0f, 0f))

    /**constructor(mesh: Mesh, position: Vector3f, rotation: Vector3f) :
    this(mesh, position, 1f, rotation)*/

    constructor(mesh: Mesh, position: Vector3f, scale: Float) :
            this(mesh, position, scale, Vector3f(0f, 0f, 0f))

    /**constructor(mesh: Mesh, scale: Float, rotation: Vector3f) :
            this(mesh, Vector3f(0f, 0f, 0f), scale, rotation)

    constructor(mesh: Mesh, position: Vector3f) :
            this(mesh, position, 1f, Vector3f(0f, 0f, 0f))

    constructor(mesh: Mesh, scale: Float) :
    this(mesh, Vector3f(0f, 0f, 0f), scale, Vector3f(0f, 0f, 0f))*/

    fun cleanup() {
        mesh.cleanup()
    }

    override fun toString(): String {
        return position.x.toString() + ":" + position.y + ":" + position.z +
                " " + rotation.x + ":" + rotation.y + ":" + rotation.z +
                " " + scale
    }
}