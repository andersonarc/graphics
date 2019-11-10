package graphics.render

import graphics.data.Camera
import graphics.data.Frame
import graphics.data.Transformation
import graphics.data.objects.Object
import graphics.data.textures.PointLight
import graphics.misc.shader
import launcher.Settings
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL30.*


class Renderer(private val frame: Frame, private val camera: Camera) {
    private val transformation = Transformation()
    private val specularPower = 10f
    private lateinit var shaderProgram: ShaderProgram

    fun init() {
        shaderProgram = ShaderProgram()
        shaderProgram.createVertexShader("vertex.vert".shader())
        shaderProgram.createFragmentShader("fragment.frag".shader())
        shaderProgram.link()
        shaderProgram.createUniform("projectionMatrix")
        shaderProgram.createUniform("modelViewMatrix")
        shaderProgram.createUniform("textureSampler")
        shaderProgram.createMaterialUniform("material")
        shaderProgram.createUniform("specularPower")
        shaderProgram.createUniform("ambientLight")
        shaderProgram.createPointLightUniform("pointLight")
    }

    private fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun render(objects: ArrayList<Object>, ambientLight: Vector3f, pointLight: PointLight) {
        clear()
        if (frame.resized) {
            glViewport(0, 0, frame.width, frame.height)
            frame.resized = false
        }

        shaderProgram.bind()
        val projectionMatrix =
            transformation.projectionMatrix(
                Settings.FOV,
                frame.width.toFloat(),
                frame.height.toFloat(),
                Settings.Z_NEAR,
                Settings.Z_FAR
            )
        shaderProgram.setUniform("projectionMatrix", projectionMatrix)

        val viewMatrix = transformation.viewMatrix(camera)

        shaderProgram.setUniform("ambientLight", ambientLight)
        shaderProgram.setUniform("specularPower", specularPower)
        val currPointLight = PointLight(pointLight)
        val position = currPointLight.position
        val aux = Vector4f(position, 1f).mul(viewMatrix)
        position.x = aux.x
        position.y = aux.y
        position.z = aux.z
        currPointLight.position = position
        shaderProgram.setUniform("pointLight", currPointLight)
        shaderProgram.setUniform("textureSampler", 0)
        for (obj in objects) {
            val mesh = obj.mesh
            val modelViewMatrix = transformation.modelViewMatrix(obj, viewMatrix)
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix)
            shaderProgram.setUniform("material", mesh.material)
            mesh.render()
        }

        shaderProgram.unbind()
    }

    fun cleanup() {
        shaderProgram.cleanup()
    }
}
