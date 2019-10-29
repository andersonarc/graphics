package sample

import data.Frame
import data.Mesh
import interfaces.Logic
import org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN
import org.lwjgl.glfw.GLFW.GLFW_KEY_UP
import org.lwjgl.opengl.GL11.glViewport
import render.Renderer
import java.awt.Color

class SampleLogic(private val frame: Frame): Logic {
    private lateinit var renderer: Renderer
    private var direction = 0
    var color = 0.0f

    override fun init() {
        renderer = Renderer(frame)
    }

    override fun input() {
        direction = when (frame.isKeyPressed(GLFW_KEY_UP)) {
            true -> 1
            else -> when (frame.isKeyPressed(GLFW_KEY_DOWN)) {
                true -> -1
                else -> 0
            }
        }
    }

    override fun update() {
        color += direction * 0.01f
        if (color > 1) {
            color = 1.0f
        } else if (color < 0) {
            color = 0.0f
        }
    }

    override fun render(mesh: Mesh) {
        if (frame.resized) {
            glViewport(0, 0, frame.width, frame.height)
            frame.resized = false
        }
        renderer.render(mesh)
    }
}