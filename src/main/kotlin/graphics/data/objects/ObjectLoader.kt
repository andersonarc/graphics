package graphics.data.objects

import graphics.misc.fileLines
import org.joml.Vector2f
import org.joml.Vector3f
import java.util.*

object ObjectLoader {
    fun loadMesh(filename: String, path: String): Mesh {
        val lines = "$path\\$filename".fileLines()
        val vertices = ArrayList<Vector3f>()
        val textures = ArrayList<Vector2f>()
        val normals = ArrayList<Vector3f>()
        val faces = ArrayList<Face>()

        for (line in lines) {
            val tokens = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            when (tokens[0]) {
                "v" -> {
                    // Geometric vertex
                    val vec3f = Vector3f(
                        tokens[1].toFloat(),
                        tokens[2].toFloat(),
                        tokens[3].toFloat()
                    )
                    vertices.add(vec3f)
                }
                "vt" -> {
                    // Texture coordinate
                    val vec2f = Vector2f(
                        tokens[1].toFloat(),
                        tokens[2].toFloat()
                    )
                    textures.add(vec2f)
                }
                "vn" -> {
                    // Vertex normal
                    val vec3fNorm = Vector3f(
                        tokens[1].toFloat(),
                        tokens[2].toFloat(),
                        tokens[3].toFloat()
                    )
                    normals.add(vec3fNorm)
                }
                "f" -> {
                    // Face
                    val face = Face(tokens[1], tokens[2], tokens[3])
                    faces.add(face)
                }
                else -> {
                }
            }
            // Ignore other line
        }
        return reorderLists(vertices, textures, normals, faces)
    }

    private fun reorderLists(
        vertices: ArrayList<Vector3f>, textures: ArrayList<Vector2f>,
        normals: ArrayList<Vector3f>, faces: ArrayList<Face>
    ): Mesh {
        val positions = FloatArray(vertices.size * 3)
        val textureCoords = FloatArray(vertices.size * 2)
        val vectorNormals = FloatArray(vertices.size * 3)
        val indices = ArrayList<Int>()
        for ((i, vertex) in vertices.withIndex()) {
            positions[i * 3] = vertex.x
            positions[i * 3 + 1] = vertex.y
            positions[i * 3 + 2] = vertex.z
        }
        for (face in faces) {
            for (indexValue in face.faceVertexIndices) {
                processFaceVertex(
                    indexValue, textures, normals,
                    indices, textureCoords, vectorNormals
                )
            }
        }
        return Mesh(positions, textureCoords, vectorNormals, indices.toIntArray())
    }

    private fun processFaceVertex(
        indexGroup: IndexGroup, textures: ArrayList<Vector2f>,
        normals: ArrayList<Vector3f>, indices: ArrayList<Int>,
        textureCoordinates: FloatArray, vectorNormals: FloatArray
    ) {
        // Set index for vertex coordinates
        val posIndex = indexGroup.indexPos
        indices.add(posIndex)

        // Reorder texture coordinates
        if (indexGroup.indexTextureCoord >= 0) {
            val textureCoord = textures[indexGroup.indexTextureCoord]
            textureCoordinates[posIndex * 2] = textureCoord.x
            textureCoordinates[posIndex * 2 + 1] = 1 - textureCoord.y
        }
        // Reorder vectorNormals
        if (indexGroup.indexNormal >= 0) {
            val vectorNormal = normals[indexGroup.indexNormal]
            vectorNormals[posIndex * 3] = vectorNormal.x
            vectorNormals[posIndex * 3 + 1] = vectorNormal.y
            vectorNormals[posIndex * 3 + 2] = vectorNormal.z
        }
    }
}