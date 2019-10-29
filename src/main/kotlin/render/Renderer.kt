package render

import data.Frame
import data.Mesh
import getResource
import org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.glDisableVertexAttribArray
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.joml.Matrix4f


class Renderer(frame: Frame) {
    private val shaderProgram = ShaderProgram()
    private val fov = Math.toRadians(60.0).toFloat()
    private val zNear = -1.05f
    private val zFar = -1.05f
    private val aspectRatio = (frame.width / frame.height).toFloat()
    private val projectionMatrix = Matrix4f().perspective(fov, aspectRatio, zNear, zFar)

    init {
        shaderProgram.createVertexShader("vertex.vert".getResource())
        shaderProgram.createFragmentShader("fragment.frag".getResource())
        shaderProgram.link()
        //shaderProgram.createUniform("projectionMatrix")
        //shaderProgram.setUniform("projectionMatrix", projectionMatrix)
    }

    fun render(mesh: Mesh) {
        clear()

        shaderProgram.bind()
        glBindVertexArray(mesh.vaoID)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glDrawElements(GL_TRIANGLES, mesh.vertexCount, GL_UNSIGNED_INT, 0)
        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindVertexArray(0)
        shaderProgram.unbind()
    }

    private fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }
}