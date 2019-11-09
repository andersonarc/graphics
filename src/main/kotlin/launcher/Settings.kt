package launcher

import graphics.data.objects.Object
import graphics.data.objects.loadMesh
import graphics.data.textures.Material
import graphics.data.textures.Texture
import org.joml.Vector4f

object Settings {
    var IP: String = ""
    var PORT: Int = 0
    var FOV: Float = 0f
    var CAMERA_POS_STEP: Float = 0f
    var MOUSE_SENSITIVITY: Float = 0f
    var Z_NEAR: Float = 0f
    var Z_FAR: Float = 0f
    var SHADER_PATH: String = ""
    var TEXTURE_PATH: String = ""
    var MODEL_PATH: String = ""
    var CAMERA_TEXTURE_FILENAME = ""
    var CAMERA_MESH_FILENAME = ""
    var DEFAULT_COLOR = Vector4f(1f, 1f, 1f, 1f)
    var TIMEOUT = 10
    var CAMERA: Object? = null
        get() {
            return if (field == null) {
                field =
                    Object(loadMesh(CAMERA_MESH_FILENAME, Material(Texture(CAMERA_TEXTURE_FILENAME), 0.5f)))
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
        shaderPath: String,
        texturePath: String,
        modelPath: String,
        cameraMeshFilename: String,
        cameraTextureFilename: String,
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
        TEXTURE_PATH = texturePath
        MODEL_PATH = modelPath
        CAMERA_TEXTURE_FILENAME = cameraTextureFilename
        CAMERA_MESH_FILENAME = cameraMeshFilename
        TIMEOUT = timeout
    }
}