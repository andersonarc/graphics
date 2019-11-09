package graphics.data.objects

import graphics.data.textures.Material
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.*
import java.util.*

class Mesh(
    positions: FloatArray,
    textureCoords: FloatArray,
    normals: FloatArray,
    indices: IntArray,
    var material: Material
) {
    private val vaoID = glGenVertexArrays()
    private val vboIDList = ArrayList<Int>()
    private var vertexCount: Int = indices.size

    constructor(positions: FloatArray, textureCoords: FloatArray, normals: FloatArray, indices: IntArray) :
            this(positions, textureCoords, normals, indices, Material())

    init {
        val posBuffer = memAllocFloat(positions.size)
        val textureCoordsBuffer = memAllocFloat(textureCoords.size)
        val indicesBuffer = memAllocInt(vertexCount)
        val normalsBuffer = memAllocFloat(normals.size)

        glBindVertexArray(vaoID)

        // Position VBO
        var vboID = glGenBuffers()
        vboIDList.add(vboID)
        posBuffer.put(positions).flip()
        glBindBuffer(GL_ARRAY_BUFFER, vboID)
        glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        // Texture coordinates VBO
        vboID = glGenBuffers()
        vboIDList.add(vboID)
        textureCoordsBuffer.put(textureCoords).flip()
        glBindBuffer(GL_ARRAY_BUFFER, vboID)
        glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_STATIC_DRAW)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0)

        // Index VBO
        vboID = glGenBuffers()
        vboIDList.add(vboID)
        indicesBuffer.put(indices).flip()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)

        // Normals VBO
        vboID = glGenBuffers()
        vboIDList.add(vboID)
        normalsBuffer.put(normals).flip()
        glBindBuffer(GL_ARRAY_BUFFER, vboID)
        glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW)
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0)

        memFree(posBuffer)
        memFree(textureCoordsBuffer)
        memFree(indicesBuffer)
        memFree(normalsBuffer)
    }

    fun render() {
        val texture = material.texture
        if (texture != null) {
            glActiveTexture(GL_TEXTURE0)
            glBindTexture(GL_TEXTURE_2D, texture.id)
        }
        glBindVertexArray(vaoID)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glEnableVertexAttribArray(2)

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glDisableVertexAttribArray(2)
        glBindVertexArray(0)
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    fun cleanup() {
        glDisableVertexAttribArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        vboIDList.forEach { glDeleteBuffers(it) }
        material.cleanup()
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoID)
    }
}