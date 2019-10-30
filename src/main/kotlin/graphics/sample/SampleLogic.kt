package graphics.sample

import graphics.data.Camera
import graphics.data.Frame
import graphics.data.MouseListener
import graphics.data.objects.Object
import graphics.data.objects.ObjectLoader.loadMesh
import graphics.data.textures.Material
import graphics.data.textures.PointLight
import graphics.data.textures.Texture
import graphics.interfaces.Logic
import graphics.render.Renderer
import launcher.Settings
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import java.util.*
import kotlin.collections.ArrayList


class SampleLogic(private val frame: Frame) : Logic {
    private var cameraInc = Vector3f()
    override val camera = Camera()
    private val renderer = Renderer(frame, camera)
    override val mouseListener = MouseListener(frame)
    private val objects = ArrayList<Object>()
    private val queue = LinkedList<Object>()
    private lateinit var ambientLight: Vector3f
    private lateinit var pointLight: PointLight

    override fun init() {
        renderer.init()
        mouseListener.init()
        val mesh = loadMesh("cube.obj", Settings.MODEL_PATH)
        val texture = Texture("text.png", Settings.TEXTURE_PATH)
        mesh.texture = texture
        val reflectance = 1f
        val material = Material(texture, reflectance)
        mesh.material = material

        ambientLight = Vector3f(0.3f, 0.3f, 0.3f)
        val lightColour = Vector3f(1f, 1f, 1f)
        val lightPosition = Vector3f(0f, 0f, 1f)
        val lightIntensity = 1.0f
        pointLight = PointLight(lightColour, lightPosition, lightIntensity)
        val att = PointLight.Attenuation(1.0f, 1.0f, 1.0f)
        pointLight.attenuation = att

        val obj = Object(mesh)
        obj.scale = 0.5f
        obj.setPosition(0f, 0f, -2f)
        objects.add(obj)
    }

    override fun input() {
        cameraInc.set(0f, 0f, 0f)
        if (frame.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1f
        } else if (frame.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1f
        }
        if (frame.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1f
        } else if (frame.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1f
        }
        if (frame.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            cameraInc.y = -1f
        } else if (frame.isKeyPressed(GLFW_KEY_SPACE)) {
            cameraInc.y = 1f
        }
    }

    override fun update() {
        camera.move(
            cameraInc.x * Settings.CAMERA_POS_STEP,
            cameraInc.y * Settings.CAMERA_POS_STEP,
            cameraInc.z * Settings.CAMERA_POS_STEP
        )
        if (mouseListener.cursorDisabled) {
            val rotVec = mouseListener.displayVec
            camera.rotate(rotVec.x * Settings.MOUSE_SENSITIVITY, rotVec.y * Settings.MOUSE_SENSITIVITY, 0f)
        }
    }

    override fun render() {
        val objects = ArrayList<Object>(objects.size + queue.size)
        objects.addAll(this.objects)
        queue.forEach {
            objects.add(it)
        }
        queue.clear()
        renderer.render(objects, ambientLight, pointLight)
    }

    override fun queue(obj: Object) {
        queue.add(obj)
    }

    override fun cleanup() {
        renderer.cleanup()
        for (obj in objects) {
            obj.mesh.cleanup()
        }
    }
}