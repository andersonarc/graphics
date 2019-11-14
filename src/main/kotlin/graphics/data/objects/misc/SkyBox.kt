package graphics.data.objects.misc

import graphics.data.objects.Mesh
import graphics.data.objects.Object

class SkyBox(meshes: Array<Mesh>) : Object(meshes) {
    constructor(mesh: Mesh) : this(arrayOf(mesh))
}