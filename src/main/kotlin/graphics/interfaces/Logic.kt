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

    fun add(obj: Object): Int

    fun modify(objectID: Int, modification: ObjectData)

    fun cleanup()
}