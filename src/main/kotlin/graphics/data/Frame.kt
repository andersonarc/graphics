package graphics.data

import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11

class Frame(val title: String, var width: Int, var height: Int, val vSync: Boolean, var window: Long) {
    var resized = false

    fun init() {
        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)
        window = GLFW.glfwCreateWindow(width, height, title, 0L, 0L)
        if (window == 0L)
            throw RuntimeException("Failed to create the GLFW window")
        GLFW.glfwMakeContextCurrent(window)
        GLFW.glfwSetFramebufferSizeCallback(window) { _, resizeWidth, resizeHeight ->
            width = resizeWidth
            height = resizeHeight
            resized = true
        }
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED)
        if (vSync) {
            GLFW.glfwSwapInterval(1)
        }
        GLFW.glfwSwapBuffers(window)
        val mode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())
        GLFW.glfwSetWindowPos(window, (mode!!.width() - width) / 2, (mode.height() - height) / 2)
        createCapabilities()
    }

    fun set() {
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glCullFace(GL11.GL_BACK)
    }

    constructor(title: String, width: Int, height: Int, vSync: Boolean): this(title, width, height, vSync, 0L)

    fun isKeyPressed(keyCode: Int): Boolean {
        return GLFW.glfwGetKey(window, keyCode) == GLFW.GLFW_PRESS
    }
}