package graphics.data.objects.misc

import graphics.data.objects.Mesh
import graphics.data.objects.Object
import graphics.data.objects.loaders.staticMesh
import java.util.*
import kotlin.streams.toList

class Scene {
    val meshes = ArrayList<Mesh>()
    val objects: List<Object>
        get() {
            return meshes.stream().flatMap { it.objects.stream() }.toList()
        }
    lateinit var skyBox: SkyBox
    //todo: SceneLight

    fun init() {
        skyBox = SkyBox("skybox".staticMesh()[0])
        skyBox.scale = 100f
    }

    fun add(obj: Object) {
        for (mesh in obj.meshes) {
            val index = meshes.indexOf(mesh)
            (if (index > -1) meshes[index].objects else {
                meshes.add(mesh)
                mesh.objects
            }).add(obj)
        }
    }

    fun add(objects: Array<Object>) {
        for (obj in objects) {
            for (mesh in obj.meshes) {
                val index = meshes.indexOf(mesh)
                (if (index > -1) meshes[index].objects else {
                    meshes.add(mesh)
                    mesh.objects
                }).add(obj)
            }
        }
    }
}