package graphics.data

import org.joml.Vector2d
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.*

class MouseListener(private val frame: Frame) {
    private var previousPos = Vector2d(-1.0, -1.0)
    private var currentPos = Vector2d()
    private var inWindow = false
    var displayVec = Vector2f()
        private set
    private var leftButtonPressed = false
    private var rightButtonPressed = false
    var cursorDisabled = true

    fun init() {
        glfwSetCursorPosCallback(frame.window) { _, xPos, yPos ->
            currentPos.x = xPos
            currentPos.y = yPos
        }
        glfwSetCursorEnterCallback(frame.window) { _, entered -> inWindow = entered }
        glfwSetMouseButtonCallback(frame.window) { _, button, action, _ ->
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS
        }
    }

    fun input() {
        displayVec.x = 0f
        displayVec.y = 0f
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow || cursorDisabled) {
            val deltaX = currentPos.x - previousPos.x
            val deltaY = currentPos.y - previousPos.y
            val rotateX = deltaX != 0.0
            val rotateY = deltaY != 0.0
            if (rotateX) {
                displayVec.y = deltaX.toFloat()
            }
            if (rotateY) {
                displayVec.x = deltaY.toFloat()
            }
        }
        previousPos.x = currentPos.x
        previousPos.y = currentPos.y
    }
}