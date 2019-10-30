package graphics.interfaces

import graphics.data.Frame

interface Engine {
    fun init(frame: Frame)

    fun exit()

    fun loop()

    fun input()

    fun update()

    fun render()
}