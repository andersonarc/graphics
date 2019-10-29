package data

import java.nio.FloatBuffer
import org.lwjgl.system.MemoryUtil
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.memAllocInt
import org.lwjgl.system.MemoryUtil.memFree
import org.lwjgl.system.MemoryUtil.memAllocFloat
import org.lwjgl.system.MemoryUtil.memAllocFloat





class Mesh(positions: FloatArray, colors: FloatArray, indices: IntArray) {
    val vaoID: Int
    private val posVboID: Int
    private val idxVboID: Int
    private val colorVboID: Int
    val vertexCount: Int = indices.size

    init {
        val verticesBuffer: FloatBuffer = MemoryUtil.memAllocFloat(positions.size)
        verticesBuffer.put(positions).flip()

        vaoID = glGenVertexArrays()
        glBindVertexArray(vaoID)

        posVboID = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, posVboID)
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)

        idxVboID = glGenBuffers()
        val indicesBuffer = memAllocInt(indices.size)
        indicesBuffer.put(indices).flip()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboID)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)
        memFree(indicesBuffer)

        colorVboID = glGenBuffers()
        val colorBuffer = memAllocFloat(colors.size)
        colorBuffer.put(colors).flip()
        glBindBuffer(GL_ARRAY_BUFFER, colorVboID)
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW)
        memFree(colorBuffer)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0)

        glBindVertexArray(0)
        memFree(verticesBuffer)
    }

    fun cleanup() {
        glDisableVertexAttribArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glDeleteBuffers(posVboID)
        glDeleteBuffers(idxVboID)
        glDeleteBuffers(colorVboID)
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoID)
    }
}