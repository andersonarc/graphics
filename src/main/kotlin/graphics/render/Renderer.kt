package graphics.render

import graphics.data.Camera
import graphics.data.Frame
import graphics.data.Transformation
import graphics.data.objects.Object
import graphics.misc.resource
import launcher.Settings
import org.lwjgl.opengl.GL30.*

class Renderer(private val frame: Frame, private val camera: Camera, private val settings: Settings) {
    private val transformation = Transformation()
    private lateinit var shaderProgram: ShaderProgram

    fun init() {
        shaderProgram = ShaderProgram()
        shaderProgram.createVertexShader("vertex.vert".resource(settings.SHADER_PATH))
        shaderProgram.createFragmentShader("fragment.frag".resource(settings.SHADER_PATH))
        shaderProgram.link()
        shaderProgram.createUniform("projectionMatrix")
        shaderProgram.createUniform("modelViewMatrix")
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

        val projectionMatrix = transformation.projectionMatrix(
            settings.FOV,
            frame.width.toFloat(),
            frame.height.toFloat(),
            settings.Z_NEAR,
            settings.Z_FAR
        )
        shaderProgram.setUniform("projectionMatrix", projectionMatrix)

        val viewMatrix = transformation.viewMatrix(camera)

        shaderProgram.setUniform("textureSampler", 0)
        for (obj in objects) {
            val mesh = obj.mesh
            val modelViewMatrix = transformation.modelViewMatrix(obj, viewMatrix)
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix)
            mesh.render()
        }
        shaderProgram.unbind()
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}
