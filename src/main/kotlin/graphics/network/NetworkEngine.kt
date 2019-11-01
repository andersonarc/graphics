package graphics.network

import graphics.data.Frame
import graphics.data.MouseListener
import graphics.data.objects.Object
import graphics.data.objects.ObjectLoader.loadMesh
import graphics.data.textures.Texture
import graphics.interfaces.Engine
import graphics.interfaces.Logic
import launcher.Settings
import network.client.Client
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallback

class NetworkEngine(frame: Frame, private val logic: Logic) : Engine {
    private var window = 0L
    private val keyCallback = KeyCallback(logic.mouseListener)
    private val errorCallback = GLFWErrorCallback.createPrint(System.err)
    private val client = Client(Settings.IP, Settings.PORT)
    private val cameraID: Int

    init {
        check(glfwInit()) { "Unable to initialize GLFW" }
        init(frame)
        cameraID = logic.add(Object(loadMesh(Settings.CAMERA_MESH_FILENAME), Texture(Settings.CAMERA_TEXTURE_FILENAME)))
        loop()
        exit()
    }


    override fun init(frame: Frame) {
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err))
        frame.init()
        logic.init()
        frame.set()
        window = frame.window
        glfwSetKeyCallback(window, keyCallback)
        glfwShowWindow(window)
    }

    override fun exit() {
        logic.cleanup()
        glfwDestroyWindow(window)
        glfwTerminate()
        keyCallback.free()
        errorCallback.free()
    }

    override fun loop() {
        var close = glfwWindowShouldClose(window)
        Thread {
            while (!close) {
                read()
            }
        }.start()
        while (!close) {
            close = glfwWindowShouldClose(window)
            glfwSwapBuffers(window)
            write()
            input()
            update()
            render()
            glfwPollEvents()
        }
    }

    override fun input() {
        logic.mouseListener.input()
        logic.input()
    }

    override fun update() {
        logic.update()
    }

    override fun render() {
        logic.render()
    }

    private fun read() {
        logic.modify(cameraID, client.read())
    }

    private fun write() {
        val camera = logic.camera
        val obj = Settings.CAMERA!!
        obj.position = camera.position
        obj.rotation = camera.rotation
        client.write(obj)
    }

    class KeyCallback(private val listener: MouseListener) : GLFWKeyCallback() {
        override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                if (glfwGetInputMode(window, GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
                    listener.cursorDisabled = false
                    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
                } else {
                    listener.cursorDisabled = true
                    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
                }
            }
        }

    }
}