package sample

import data.*
import interfaces.Logic
import org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN
import org.lwjgl.glfw.GLFW.GLFW_KEY_UP
import render.Renderer
import org.lwjgl.glfw.GLFW.GLFW_KEY_X
import org.lwjgl.glfw.GLFW.GLFW_KEY_Z
import org.lwjgl.glfw.GLFW.GLFW_KEY_Q
import org.lwjgl.glfw.GLFW.GLFW_KEY_A
import org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT
import org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT

class SampleLogic(private val frame: Frame): Logic {
    private var displayXInc = 0
    private var displayYInc = 0
    private var displayZInc = 0
    private var scaleInc = 0
    private val renderer = Renderer(frame)
    private val mouseListener = MouseListener(frame)
    private val objects = ArrayList<Object>()

    override fun init() {
        renderer.init()
        mouseListener.init()
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
        val obj = Object(Mesh(positions, textureCoords, indices, Texture("src\\main\\resources\\text.png")))
        obj.setPosition(0f, 0f, -2f)
        objects.add(obj)
    }

    override fun input() {
        displayYInc = 0
        displayXInc = 0
        displayZInc = 0
        scaleInc = 0
        displayZInc = when (frame.isKeyPressed(GLFW_KEY_UP)) {
            true -> 1
            else -> when (frame.isKeyPressed(GLFW_KEY_DOWN)) {
                true -> -1
                else -> when (frame.isKeyPressed(GLFW_KEY_LEFT)) {
                    true -> 1
                    else -> when (frame.isKeyPressed(GLFW_KEY_RIGHT)) {
                        true -> -1
                        else -> when (frame.isKeyPressed(GLFW_KEY_A)) {
                            true -> 1
                            else -> when (frame.isKeyPressed(GLFW_KEY_Q)) {
                                true -> 1
                                else -> 0
                            }
                        }
                    }
                }
            }
        }
        scaleInc = when (frame.isKeyPressed(GLFW_KEY_Z)) {
            true -> -1
            else -> when (frame.isKeyPressed(GLFW_KEY_X)) {
                true -> 1
                else -> 0
            }
        }
    }

    override fun update() {
        for (obj in objects) {
            val itemPos = obj.position
            val posx = itemPos.x + displayXInc * 0.01f
            val posy = itemPos.y + displayYInc * 0.01f
            val posz = itemPos.z + displayZInc * 0.01f
            obj.setPosition(posx, posy, posz)

            obj.scale += scaleInc * 0.05f
            if (obj.scale < 0) {
                obj.scale = 0f
            }

            obj.rotation.x += 1.5f
            if (obj.rotation.x > 360) {
                obj.rotation.x = 0f
            }
        }
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