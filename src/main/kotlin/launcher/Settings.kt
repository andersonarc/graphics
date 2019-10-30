package launcher

import graphics.data.objects.Object
import graphics.data.objects.ObjectLoader.loadMesh
import graphics.data.textures.Texture
import org.joml.Vector4f

object Settings {
    var IP: String = ""
    var PORT: Int = 0
    var FOV: Float = 0.0f
    var CAMERA_POS_STEP: Float = 0.0f
    var MOUSE_SENSITIVITY: Float = 0.0f
    var Z_NEAR: Float = 0.0f
    var Z_FAR: Float = 0.0f
    var PATH: String = ""
    var SHADER_PATH: String = ""
    var TEXTURE_PATH: String = ""
    var MODEL_PATH: String = ""
    var CAMERA_TEXTURE_FILENAME = ""
    var CAMERA_MESH_FILENAME = ""
    var DEFAULT_COLOR = Vector4f(1.0f, 1.0f, 1.0f, 1.0f)
    private var nullCamera = true
    var CAMERA: Object? = null
        get() {
            return if (nullCamera) {
                CAMERA =
                    Object(
                        loadMesh(CAMERA_MESH_FILENAME, MODEL_PATH),
                        Texture(CAMERA_TEXTURE_FILENAME, TEXTURE_PATH)
                    )
                field
            } else {
                field
            }
        }

    fun load(
        ip: String,
        port: Int,
        fov: Float,
        cameraPosStep: Float,
        mouseSensitivity: Float,
        zNear: Float,
        zFar: Float,
        path: String,
        shaderPath: String,
        texturePath: String,
        modelPath: String,
        cameraMeshFilename: String,
        cameraTextureFilename: String
    ) {

        IP = ip
        PORT = port
        FOV = fov
        CAMERA_POS_STEP = cameraPosStep
        MOUSE_SENSITIVITY = mouseSensitivity
        Z_NEAR = zNear
        Z_FAR = zFar
        PATH = path
        SHADER_PATH = shaderPath
        TEXTURE_PATH = texturePath
        MODEL_PATH = modelPath
        CAMERA_TEXTURE_FILENAME = cameraTextureFilename
        CAMERA_MESH_FILENAME = cameraMeshFilename
    }
}