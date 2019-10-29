package sample

import interfaces.Engine
import data.Frame
import data.Mesh
import interfaces.Logic
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray
import org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer




class SampleEngine(private val frame: Frame, private val logic: Logic): Engine {
    private var window = 0L
    private val keyCallback = KeyCallback()
    private val errorCallback = GLFWErrorCallback.createPrint(System.err)

    init {
        check(glfwInit()) { "Unable to initialize GLFW" }
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err))
        initWindow()
        glfwSetKeyCallback(window, keyCallback)
        renderWindow()
        glfwShowWindow(window)
    }

    private fun initWindow() {
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)
        window = glfwCreateWindow(frame.width, frame.height, frame.title, 0L, 0L)
        if (window == 0L)
            throw RuntimeException("Failed to create the GLFW window")
        frame.window = window
        glfwMakeContextCurrent(window)
        glfwSetFramebufferSizeCallback(window) { _, resizeWidth, resizeHeight -> //callback for checking window resizing
            frame.width = resizeWidth
            frame.height = resizeHeight
            frame.resized = true
        }
    }

    private fun renderWindow() {
        if (frame.vSync) { glfwSwapInterval(1) }
        glfwSwapBuffers(window)
        val mode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        glfwSetWindowPos(window, (mode!!.width() - frame.width) / 2, (mode.height() - frame.height) / 2)
    }

    override fun run() {
        createCapabilities()
        logic.init()
        loop()
        glfwDestroyWindow(window)
        glfwTerminate()
        keyCallback.free()
        errorCallback.free()
    }


    override fun loop() {
        GL11.glClearColor(0.3f, 0.3f, 0.3f, 0.1f)
        while (!glfwWindowShouldClose(window)) {
            glfwSwapBuffers(window)
            execute()
            glfwPollEvents()
        }
    }

    override fun execute() {
        logic.input()
        logic.update()
        val positions = floatArrayOf(
            -0.5f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f,  0.5f, 0.0f)
        val indices = intArrayOf(0, 1, 3, 3, 1, 2)
        val colors = floatArrayOf(
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f)
        logic.render(Mesh(positions, colors, indices))
    }

    class KeyCallback: GLFWKeyCallback() {
        override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true)
            }
        }

    }
}