package render

import data.Camera
import data.Frame
import data.Object
import data.Transformation
import getResource
import org.lwjgl.opengl.GL30.*

class Renderer(private val frame: Frame) {
    private val camera = Camera()
    private val fov = Math.toRadians(60.0).toFloat()
    private val zNear = 0.49f
    private val zFar = 1000f
    private val transformation = Transformation()
    private lateinit var shaderProgram: ShaderProgram

    fun init() {
        shaderProgram = ShaderProgram()
        shaderProgram.createVertexShader("vertex.vert".getResource())
        shaderProgram.createFragmentShader("fragment.frag".getResource())
        shaderProgram.link()
        shaderProgram.createUniform("projectionMatrix")
        shaderProgram.createUniform("worldMatrix")
        shaderProgram.createUniform("textureSampler")
    }

    private fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun render(objects: ArrayList<Object>) {
        clear()
        if (frame.resized) {
            glViewport(0, 0, frame.width, frame.height)
            frame.resized = false
        }
        shaderProgram.bind()

        val projectionMatrix = transformation.projectionMatrix(fov, frame.width.toFloat(), frame.height.toFloat(), zNear, zFar)
        shaderProgram.setUniform("projectionMatrix", projectionMatrix)

        val viewMatrix = transformation.viewMatrix(camera)

        shaderProgram.setUniform("textureSampler", 0)
        for (obj in objects) {
            //val modelViewMatrix = transformation.modelViewMatrix(obj, viewMatrix)
            //shaderProgram.setUniform("modelViewMatrix", modelViewMatrix)
            shaderProgram.setUniform("worldMatrix", transformation.worldMatrix(obj.position, obj.rotation, obj.scale))
            obj.mesh.render()
        }
        shaderProgram.unbind()
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}
