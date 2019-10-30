package launcher

import graphics.data.objects.Object
import graphics.data.objects.ObjectLoader.loadMesh
import graphics.data.objects.Texture

data class Settings(
    val IP: String,
    val PORT: Int,
    val FOV: Float,
    val CAMERA_POS_STEP: Float,
    val MOUSE_SENSITIVITY: Float,
    val Z_NEAR: Float,
    val Z_FAR: Float,
    val PATH: String,
    val SHADER_PATH: String,
    val TEXTURE_PATH: String,
    val MODEL_PATH: String,
    private val CAMERA_MESH_NAME: String,
    private val CAMERA_TEXTURE_NAME: String
) {

    lateinit var CAMERA: Object
        private set

    fun init() {
        CAMERA = Object(loadMesh(CAMERA_MESH_NAME, MODEL_PATH))
        CAMERA.mesh.texture = Texture(CAMERA_TEXTURE_NAME, TEXTURE_PATH)
    }
}