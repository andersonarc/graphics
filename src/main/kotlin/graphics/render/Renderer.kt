package graphics.render

import graphics.data.Camera
import graphics.data.Frame
import graphics.data.Transformation
import graphics.data.objects.loaders.shader
import graphics.data.objects.misc.Scene
import graphics.data.objects.optimization.FrustumCullingFilter
import graphics.data.textures.PointLight
import launcher.Settings.FOV
import launcher.Settings.Z_FAR
import launcher.Settings.Z_NEAR
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL30.*

class Renderer(private val frame: Frame, private val camera: Camera, private val scene: Scene = Scene()) {
    private val frustumCullingFilter = FrustumCullingFilter()
    private val transformation = Transformation()
    private val specularPower = 10f
    private lateinit var sceneShaderProgram: ShaderProgram
    private lateinit var skyBoxShaderProgram: ShaderProgram

    fun init() {
        scene.init()
        setupSceneShader()
        setupSkyBoxShader()
    }

    private fun setupSceneShader() {
        sceneShaderProgram = ShaderProgram()
        sceneShaderProgram.createVertexShader("scene.vert".shader())
        sceneShaderProgram.createFragmentShader("scene.frag".shader())
        sceneShaderProgram.link()
        sceneShaderProgram.createUniform("projectionMatrix")
        sceneShaderProgram.createUniform("modelViewMatrix")
        sceneShaderProgram.createUniform("textureSampler")
        sceneShaderProgram.createMaterialUniform("material")
        sceneShaderProgram.createUniform("specularPower")
        sceneShaderProgram.createUniform("ambientLight")
        sceneShaderProgram.createPointLightUniform("pointLight")
    }

    private fun setupSkyBoxShader() {
        skyBoxShaderProgram = ShaderProgram()
        skyBoxShaderProgram.createVertexShader("skybox.vert".shader())
        skyBoxShaderProgram.createFragmentShader("skybox.frag".shader())
        skyBoxShaderProgram.link()
        skyBoxShaderProgram.createUniform("projectionMatrix")
        skyBoxShaderProgram.createUniform("modelViewMatrix")
        skyBoxShaderProgram.createUniform("textureSampler")
        skyBoxShaderProgram.createUniform("ambientLight")
    }

    private fun clear() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun render(ambientLight: Vector3f, pointLight: PointLight) {
        val projectionMatrix =
            transformation.projectionMatrix(
                FOV,
                frame.width.toFloat(),
                frame.height.toFloat(),
                Z_NEAR,
                Z_FAR
            )
        renderScene(ambientLight, pointLight, projectionMatrix)
        renderSkyBox(projectionMatrix)
    }

    private fun renderScene(ambientLight: Vector3f, pointLight: PointLight, projectionMatrix: Matrix4f) {
        clear()
        if (frame.resized) {
            glViewport(0, 0, frame.width, frame.height)
            frame.resized = false
        }
        sceneShaderProgram.bind()
        sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix)
        val viewMatrix = transformation.viewMatrix(camera)
        sceneShaderProgram.setUniform("ambientLight", ambientLight)
        sceneShaderProgram.setUniform("specularPower", specularPower)
        val currPointLight = PointLight(pointLight)
        val position = currPointLight.position
        val aux = Vector4f(position, 1f).mul(viewMatrix)
        position.x = aux.x
        position.y = aux.y
        position.z = aux.z
        currPointLight.position = position
        sceneShaderProgram.setUniform("pointLight", currPointLight)
        sceneShaderProgram.setUniform("textureSampler", 0)
        frustumCullingFilter.updateFrustum(projectionMatrix, viewMatrix)
        frustumCullingFilter.filter(scene.meshes)
        for (mesh in scene.meshes) {
            sceneShaderProgram.setUniform("material", mesh.material)
            mesh.renderList {
                sceneShaderProgram.setUniform("modelViewMatrix", transformation.modelViewMatrix(it, viewMatrix))
            }
        }
        /**for (obj in scene.objects) {
        if (!obj.insideFrustum && obj.frustumCulling) continue
            for (mesh in obj.meshes) {
                val modelViewMatrix = transformation.modelViewMatrix(obj, viewMatrix)
                sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix)
                sceneShaderProgram.setUniform("material", mesh.material)
                mesh.render()
            }
        }*/

        sceneShaderProgram.unbind()
    }

    private fun renderSkyBox(projectionMatrix: Matrix4f) {
        skyBoxShaderProgram.bind()
        skyBoxShaderProgram.setUniform("textureSampler", 0)
        skyBoxShaderProgram.setUniform("projectionMatrix", projectionMatrix)
        val skyBox = scene.skyBox
        val viewMatrix = transformation.viewMatrix(camera)
        viewMatrix.m30(0f)
        viewMatrix.m31(0f)
        viewMatrix.m32(0f)
        val modelViewMatrix = transformation.modelViewMatrix(skyBox, viewMatrix)
        skyBoxShaderProgram.setUniform("modelViewMatrix", modelViewMatrix)
        //todo: skyBoxShaderProgram.setUniform("ambientLight", scene.getSceneLight().getAmbientLight())
        skyBoxShaderProgram.setUniform("ambientLight", Vector3f(1f, 1f, 1f))
        for (mesh in scene.skyBox.meshes) {
            mesh.render()
        }
        skyBoxShaderProgram.unbind()
    }

    fun cleanup() {
        sceneShaderProgram.cleanup()
    }
}
