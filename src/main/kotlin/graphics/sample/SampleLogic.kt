package graphics.sample

import graphics.data.Camera
import graphics.data.Frame
import graphics.data.MouseListener
import graphics.data.objects.Object
import graphics.data.objects.ObjectData
import graphics.data.objects.loaders.staticMesh
import graphics.data.objects.misc.Scene
import graphics.data.textures.PointLight
import graphics.data.textures.PointLight.Attenuation
import graphics.interfaces.Logic
import graphics.render.Renderer
import launcher.Settings
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*

class SampleLogic(private val frame: Frame) : Logic {
    private val modificationsMap = HashMap<Int, Int>() // hashcode index | local index
    override val mouseListener = MouseListener(frame)
    private lateinit var ambientLight: Vector3f
    private lateinit var pointLight: PointLight
    private var cameraInc = Vector3f()
    override val camera = Camera()
    private val scene = Scene()
    private val objects = scene.objects
    private val renderer = Renderer(frame, camera, scene)

    override fun init() {
        renderer.init()
        mouseListener.init()
        camera.position.z = -12f
        camera.position.y = 3f
        scene.add(
            Object(
                "house".staticMesh(),
                Vector3f(0f, 0.5f, -2f),
                0.5f
            )
        )
        val worldSize = Settings.WORLD_SIZE / 2
        for (x in -worldSize..worldSize) {
            for (z in -worldSize..worldSize) {
                scene.add(
                    Object(
                        "cube".staticMesh(),
                        Vector3f(x.toFloat(), 0f, z.toFloat()),
                        0.5f
                    )
                )
            }
        }
        //todo: "monster".animatedObject()
        ambientLight = Vector3f(0.8f, 0.8f, 0.8f)
        val lightColor = Vector3f(1f, 1f, 1f)
        val lightPosition = Vector3f(0f, 0f, 3f)
        val lightIntensity = 1f
        pointLight = PointLight(lightColor, lightPosition, lightIntensity)
        val att = Attenuation(0f, 0f, 1f)
        pointLight.attenuation = att
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
        renderer.render(ambientLight, pointLight)
    }

    override fun modify(modifications: Array<ObjectData>) {
        for (modification in modifications) {
            modify(modification)
        }
    }

    override fun modify(modification: ObjectData) {
        if (!modificationsMap.containsKey(modification.id)) {
            modification(modification.id, Object(modification.mesh))
        }
        val obj = objects[modificationsMap[modification.id]!!]
        obj.position = modification.position
        obj.rotation = modification.rotation
        obj.scale = modification.scale
    }

    override fun modification(hashcode: Int, obj: Object): Int {
        scene.add(obj)
        val localIndex = objects.indexOf(obj)
        modificationsMap[hashcode] = localIndex
        return localIndex
    }

    override fun cleanup() {
        renderer.cleanup()
        for (obj in objects) {
            obj.cleanup()
        }
    }
}