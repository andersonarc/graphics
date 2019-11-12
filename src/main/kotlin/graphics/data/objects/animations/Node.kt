package graphics.data.objects.animations

import org.joml.Matrix4f
import java.util.*


class Node(val name: String, val parent: Node?) {
    val children = ArrayList<Node>()
    val transformations = ArrayList<Matrix4f>()
    val animationFrames: Int
        get() {
            var numFrames = this.transformations.size
            for (child in children) {
                val childFrame = child.animationFrames
                numFrames = numFrames.coerceAtLeast(childFrame)
            }
            return numFrames
        }

    fun addChild(node: Node) {
        this.children.add(node)
    }

    fun addTransformation(transformation: Matrix4f) {
        transformations.add(transformation)
    }

    fun findByName(target: String): Node? {
        return if (this.name == target) {
            this
        } else {
            var result: Node? = null
            for (child in children) {
                result = child.findByName(target)
                if (result != null) {
                    break
                }
            }
            result
        }
    }

    fun getParentTransforms(framePosition: Int): Matrix4f {
        val parentTransform = Matrix4f(parent?.getParentTransforms(framePosition))
        val transformationSize = transformations.size
        val nodeTransform = when (framePosition < transformationSize) {
            true -> transformations[framePosition]
            else -> when (transformationSize > 0) {
                true -> transformations[transformationSize - 1]
                else -> Matrix4f()
            }
        }
        return parentTransform.mul(nodeTransform)
    }
}