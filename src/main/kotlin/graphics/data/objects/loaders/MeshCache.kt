package graphics.data.objects.loaders

import graphics.data.objects.Mesh
import java.util.*


object MeshCache {
    private val meshes = HashMap<String, Array<Mesh>>()

    fun staticMesh(path: String, texturesDirectory: String): Array<Mesh> {
        var load = meshes[path]
        if (load == null) {
            load = loadStaticMesh(path, texturesDirectory)
            meshes[path] = load
        }
        return load
    }

    fun animatedMesh(path: String): Array<Mesh>? {
        return meshes[path]
    }

    fun putAnimatedMesh(path: String, meshes: Array<Mesh>) {
        this.meshes[path] = meshes
    }
}