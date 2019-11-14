package graphics.data.objects

import org.joml.Quaternionf
import org.joml.Vector3f

data class ObjectData(
    var id: Int,
    var position: Vector3f,
    var rotation: Quaternionf,
    var scale: Float,
    var mesh: Mesh
)