package launcher

import graphics.data.objects.Mesh
import graphics.data.objects.loaders.TextureFormat
import graphics.data.objects.loaders.loadStaticMesh
import org.joml.Vector4f

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
                field = loadStaticMesh(CAMERA_FILENAME, TextureFormat.PNG)[0]
                field
            } else {
                field
            }
        }
    var MAX_WEIGHTS = 4

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
        timeout: Int
    ) {
        IP = ip
        PORT = port
        FOV = fov
        CAMERA_POS_STEP = cameraPosStep
        MOUSE_SENSITIVITY = mouseSensitivity
        Z_NEAR = zNear
        Z_FAR = zFar
        SHADER_PATH = shaderPath
        MODEL_PATH = modelPath
        CAMERA_FILENAME = cameraFilename
        TIMEOUT = timeout
    }
}