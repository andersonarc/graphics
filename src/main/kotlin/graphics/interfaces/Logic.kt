package graphics.interfaces

import graphics.data.Camera
import graphics.data.MouseListener
import graphics.data.objects.Object

interface Logic {
    val camera: Camera
    val mouseListener: MouseListener

    fun init()

    fun input()

    fun update()

    fun render()

    fun queue(obj: Object)

    fun cleanup()
}