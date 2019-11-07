package graphics.interfaces

import graphics.data.Camera
import graphics.data.MouseListener
import graphics.data.objects.Object
import graphics.data.objects.ObjectData

interface Logic {
    val camera: Camera
    val mouseListener: MouseListener

    fun init()

    fun input()

    fun update()

    fun render()

    fun modification(hashcode: Int, obj: Object): Int

    fun modify(modifications: Array<ObjectData>)

    fun modify(modification: ObjectData)

    fun cleanup()
}