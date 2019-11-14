package launcher

import graphics.data.objects.Mesh
import graphics.data.objects.loaders.staticMesh
import org.joml.Vector4f
import java.io.File

object Settings {
    var IP = ""
    var PORT = 0
    var FOV = 0f
    var CAMERA_POS_STEP = 0f
    var MOUSE_SENSITIVITY = 0f
    var Z_NEAR = 0f
    var Z_FAR = 0f
    var SHADER_PATH = ""
    var MODEL_PATH = ""
    private var CAMERA_FILENAME = ""
    var DEFAULT_COLOR = Vector4f(1f, 1f, 1f, 1f)
    var TIMEOUT = 10
    var CAMERA: Mesh? = null
        get() {
            return if (field == null) {
                field = CAMERA_FILENAME.staticMesh()[0]
                field
            } else {
                field
            }
        }
    var MAX_WEIGHTS = 4
    var OFFLINE = true
    var WORLD_SIZE = 1000

    fun load(
        ip: String,
        port: Int,
        fov: Float,
        cameraPosStep: Float,
        mouseSensitivity: Float,
        zNear: Float,
        zFar: Float,
        shaderPath: String,
        modelPath: String,
        cameraFilename: String,
        timeout: Int,
        offline: Boolean,
        worldSize: Int
    ) {
        IP = ip
        PORT = port
        FOV = fov
        CAMERA_POS_STEP = cameraPosStep
        MOUSE_SENSITIVITY = mouseSensitivity
        Z_NEAR = zNear
        Z_FAR = zFar
        SHADER_PATH = shaderPath
        MODEL_PATH = File(modelPath).absolutePath
        CAMERA_FILENAME = cameraFilename
        TIMEOUT = timeout
        OFFLINE = offline
        WORLD_SIZE = worldSize
    }
}