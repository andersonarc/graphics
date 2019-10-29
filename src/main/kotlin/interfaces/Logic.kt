package interfaces

import data.Mesh

interface Logic {
    fun init()

    fun input()

    fun update()

    fun render(mesh: Mesh)
}