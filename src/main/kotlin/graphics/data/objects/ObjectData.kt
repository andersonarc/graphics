package graphics.data.objects

import org.joml.Vector3f

data class ObjectData(
    var id: Int,
    var position: Vector3f,
    var rotation: Vector3f,
    var scale: Float,
    var mesh: Mesh
)