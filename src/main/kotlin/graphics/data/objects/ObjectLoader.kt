package graphics.data.objects

import graphics.data.textures.Material
import graphics.misc.fileLines
import launcher.Settings
import org.joml.Vector2f
import org.joml.Vector3f
import java.util.*


fun loadMesh(filename: String): Mesh {
    val lines = "${Settings.MODEL_PATH}$filename".fileLines()
    val vertices = ArrayList<Vector3f>()
    val textures = ArrayList<Vector2f>()
    val normals = ArrayList<Vector3f>()
    val faces = ArrayList<Face>()

    for (line in lines) {
        val tokens = line.split(" ")
        when (tokens[0]) {
            "v" -> {
                // Geometric vertex
                val vec3f = Vector3f(
                    java.lang.Float.parseFloat(tokens[1]),
                    java.lang.Float.parseFloat(tokens[2]),
                    java.lang.Float.parseFloat(tokens[3])
                )
                vertices.add(vec3f)
            }
            "vt" -> {
                // Texture coordinate
                val vec2f = Vector2f(
                    java.lang.Float.parseFloat(tokens[1]),
                    java.lang.Float.parseFloat(tokens[2])
                )
                textures.add(vec2f)
            }
            "vn" -> {
                // Vertex normal
                val vec3fNorm = Vector3f(
                    java.lang.Float.parseFloat(tokens[1]),
                    java.lang.Float.parseFloat(tokens[2]),
                    java.lang.Float.parseFloat(tokens[3])
                )
                normals.add(vec3fNorm)
            }
            "f" -> {
                val face = Face(tokens[1], tokens[2], tokens[3])
                faces.add(face)
            }
            else -> {
                // Ignore other lines
            }
        }
    }
    return reorderLists(vertices, textures, normals, faces)
}

fun loadMesh(filename: String, material: Material): Mesh {
    val mesh = loadMesh(filename)
    mesh.material = material
    return mesh
}

private fun reorderLists(
    posList: ArrayList<Vector3f>, textCoordList: ArrayList<Vector2f>,
    normList: ArrayList<Vector3f>, facesList: ArrayList<Face>
): Mesh {
    val indices = ArrayList<Int>()
    // Create position array in the order it has been declared
    val posArr = FloatArray(posList.size * 3)
    for ((i, pos) in posList.withIndex()) {
        posArr[i * 3] = pos.x
        posArr[i * 3 + 1] = pos.y
        posArr[i * 3 + 2] = pos.z
    }
    val textCoordArr = FloatArray(posList.size * 2)
    val normArr = FloatArray(posList.size * 3)
    for (face in facesList) {
        val faceVertexIndices = face.faceVertexIndices
        for (indValue in faceVertexIndices) {
            processFaceVertex(
                indValue, textCoordList, normList,
                indices, textCoordArr, normArr
            )
        }
    }
    val indicesArr = indices.stream().mapToInt { v: Int -> v }.toArray()
    return Mesh(posArr, textCoordArr, normArr, indicesArr)
}

private fun processFaceVertex(
    indices: IndexGroup, textCoordList: ArrayList<Vector2f>,
    normList: ArrayList<Vector3f>, indicesList: ArrayList<Int>,
    texCoordArr: FloatArray, normArr: FloatArray
) {
    // Set index for vertex coordinates
    val posIndex = indices.indexPos
    indicesList.add(posIndex)

    // Reorder texture coordinates
    if (indices.indexTextureCoord >= 0) {
        val textCoord = textCoordList[indices.indexTextureCoord]
        texCoordArr[posIndex * 2] = textCoord.x
        texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y
    }
    if (indices.indexNormal >= 0) {
        // Reorder vector normals
        val vecNorm = normList[indices.indexNormal]
        normArr[posIndex * 3] = vecNorm.x
        normArr[posIndex * 3 + 1] = vecNorm.y
        normArr[posIndex * 3 + 2] = vecNorm.z
    }
}