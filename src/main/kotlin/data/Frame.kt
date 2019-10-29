package data

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.glfwGetKey
import java.awt.Color


class Frame(val title: String, var width: Int, var height: Int, val vSync: Boolean, var window: Long) {
    var resized = false
    var color = Color(0)

    constructor(title: String, width: Int, height: Int, vSync: Boolean): this(title, width, height, vSync, 0L)

    fun isKeyPressed(keyCode: Int): Boolean {
        return glfwGetKey(window, keyCode) == GLFW_PRESS
    }
}