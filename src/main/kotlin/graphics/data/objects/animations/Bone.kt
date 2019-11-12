package graphics.data.objects.animations

import org.joml.Matrix4f

data class Bone(val boneId: Int, val boneName: String, val offsetMatrix: Matrix4f)