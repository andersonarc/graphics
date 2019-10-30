package graphics.data.objects

import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.*
import java.util.*


class Mesh(positions: FloatArray, textureCoords: FloatArray, normals: FloatArray, indices: IntArray) {
    private val vaoID = glGenVertexArrays()
    private val vboIDList = ArrayList<Int>()
    private var vertexCount: Int = indices.size
    var texture: Texture? = null

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
        if (texture != null) {
            renderTexture()
        } else {
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
    }

    private fun renderTexture() {
        glActiveTexture(GL_TEXTURE0)
        texture!!.bind()
        glBindVertexArray(vaoID)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glEnableVertexAttribArray(2)

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glDisableVertexAttribArray(2)
        glBindVertexArray(0)
        texture!!.unbind()
    }

    fun cleanup() {
        glDisableVertexAttribArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        vboIDList.forEach { glDeleteBuffers(it) }
        texture?.cleanup()
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoID)
    }
}