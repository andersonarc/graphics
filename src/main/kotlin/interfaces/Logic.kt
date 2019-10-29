package interfaces

import data.MouseListener

interface Logic {
    val mouseListener: MouseListener

    fun init()

    fun input()

    fun update()

    fun render()

    fun cleanup()
}