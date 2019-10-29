package data

import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.memAllocInt
import org.lwjgl.system.MemoryUtil.memFree
import org.lwjgl.system.MemoryUtil.memAllocFloat
import java.util.ArrayList


class Mesh(positions: FloatArray, textureCoords: FloatArray, indices: IntArray, private val texture: Texture) {
    private val vaoID = glGenVertexArrays()
    private val vboIDList = ArrayList<Int>()
    private var vertexCount: Int = indices.size

    init {
        val posBuffer = memAllocFloat(positions.size)
        val textureCoordsBuffer = memAllocFloat(textureCoords.size)
        val indicesBuffer = memAllocInt(vertexCount)

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

        memFree(posBuffer)
        memFree(textureCoordsBuffer)
        memFree(indicesBuffer)
    }

    fun render() {
        glActiveTexture(GL_TEXTURE0)

        texture.bind()
        glBindVertexArray(vaoID)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindVertexArray(0)
        texture.unbind()
    }

    fun cleanup() {
        glDisableVertexAttribArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        vboIDList.forEach { glDeleteBuffers(it) }
        texture.cleanup()
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoID)
    }
}