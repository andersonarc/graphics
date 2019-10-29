package sample

import data.*
import interfaces.Logic
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import render.Renderer

class SampleLogic(private val frame: Frame): Logic {
    private var cameraInc = Vector3f()
    private val cameraPosStep = 0.5f
    private val mouseSensitivity = 0.2f
    private val camera = Camera()
    private val renderer = Renderer(frame, camera)
    override val mouseListener = MouseListener(frame)
    private val objects = ArrayList<Object>()

    override fun init() {
        renderer.init()
        mouseListener.init()
        val cubes = 5
        val positions = floatArrayOf(
            // V0
            -0.5f, 0.5f, 0.5f,
            // V1
            -0.5f, -0.5f, 0.5f,
            // V2
            0.5f, -0.5f, 0.5f,
            // V3
            0.5f, 0.5f, 0.5f,
            // V4
            -0.5f, 0.5f, -0.5f,
            // V5
            0.5f, 0.5f, -0.5f,
            // V6
            -0.5f, -0.5f, -0.5f,
            // V7
            0.5f, -0.5f, -0.5f,
            // For text coords in top face
            // V8: V4 repeated
            -0.5f, 0.5f, -0.5f,
            // V9: V5 repeated
            0.5f, 0.5f, -0.5f,
            // V10: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V11: V3 repeated
            0.5f, 0.5f, 0.5f,
            // For text coords in right face
            // V12: V3 repeated
            0.5f, 0.5f, 0.5f,
            // V13: V2 repeated
            0.5f, -0.5f, 0.5f,
            // For text coords in left face
            // V14: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V15: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // For text coords in bottom face
            // V16: V6 repeated
            -0.5f, -0.5f, -0.5f,
            // V17: V7 repeated
            0.5f, -0.5f, -0.5f,
            // V18: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // V19: V2 repeated
            0.5f, -0.5f, 0.5f
        )
        val textureCoords = floatArrayOf(
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,
            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            // For text coords in top face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,
            // For text coords in right face
            0.0f, 0.0f,
            0.0f, 0.5f,
            // For text coords in left face
            0.5f, 0.0f,
            0.5f, 0.5f,
            // For text coords in bottom face
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f
        )
        val indices = intArrayOf(
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            8, 10, 11, 9, 8, 11,
            // Right face
            12, 13, 7, 5, 12, 7,
            // Left face
            14, 15, 6, 4, 14, 6,
            // Bottom face
            16, 18, 19, 17, 16, 19,
            // Back face
            4, 6, 7, 5, 4, 7
        )
        val mesh = Mesh(positions, textureCoords, indices, Texture("src\\main\\resources\\text.png"))
        for (x in 0..cubes) {
            for (z in 0..cubes) {
                createCube(x.toFloat(), 0f, z.toFloat(), mesh)
            }
        }
    }

    private fun createCube(x: Float, y: Float, z: Float, mesh: Mesh) {
        val obj = Object(mesh)
        obj.setPosition(x, y, z)
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
        // Update camera position
        camera.move(
            cameraInc.x * cameraPosStep,
            cameraInc.y * cameraPosStep,
            cameraInc.z * cameraPosStep
        )

        // Update camera based on mouse
        val rotVec = mouseListener.displayVec
        camera.rotate(rotVec.x * mouseSensitivity, rotVec.y * mouseSensitivity, 0f)
    }

    override fun render() {
        renderer.render(objects)
    }

    override fun cleanup() {
        renderer.cleanup()
        for (obj in objects) {
            obj.mesh.cleanup()
        }
    }
}