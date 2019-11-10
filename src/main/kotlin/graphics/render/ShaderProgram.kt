package graphics.render

import graphics.data.textures.Material
import graphics.data.textures.PointLight
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryUtil.memAllocFloat
import org.lwjgl.system.MemoryUtil.memFree

class ShaderProgram {
    private var programId = 0
    private var vertexShaderId = 0
    private var fragmentShaderId = 0
    private val uniforms = HashMap<String, Int>()

    init {
        programId = glCreateProgram()
        if (programId == 0) {
            throw Exception("Could not create Shader")
        }
    }

    fun createVertexShader(shaderCode: String) {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER)
    }

    fun createFragmentShader(shaderCode: String) {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER)
    }

    private fun createShader(shaderCode: String, shaderType: Int): Int {
        val shaderId = glCreateShader(shaderType)
        if (shaderId == 0) {
            throw Exception("Error creating shader. Type: $shaderType")
        }

        glShaderSource(shaderId, shaderCode)
        glCompileShader(shaderId)
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024))
        }

        glAttachShader(programId, shaderId)
        return shaderId
    }

    fun createUniform(uniformName: String) {
        val uniformLocation = glGetUniformLocation(programId, uniformName)
        if (uniformLocation < 0) {
            throw Exception("Could not find uniform: $uniformName")
        }
        uniforms[uniformName] = uniformLocation
    }

    fun createPointLightUniform(uniformName: String) {
        createUniform("$uniformName.color")
        createUniform("$uniformName.position")
        createUniform("$uniformName.intensity")
        createUniform("$uniformName.att.constant")
        createUniform("$uniformName.att.linear")
        createUniform("$uniformName.att.exponent")
    }

    fun createMaterialUniform(uniformName: String) {
        createUniform("$uniformName.ambient")
        createUniform("$uniformName.diffuse")
        createUniform("$uniformName.specular")
        createUniform("$uniformName.hasTexture")
        createUniform("$uniformName.reflectance")
    }

    fun setUniform(uniformName: String, value: Matrix4f) {
        val fb = memAllocFloat(16)
        value.get(fb)
        uniforms[uniformName]?.let { glUniformMatrix4fv(it, false, fb) }
        memFree(fb)
    }

    fun setUniform(uniformName: String, value: Vector3f) {
        uniforms[uniformName]?.let { glUniform3f(it, value.x, value.y, value.z) }
    }

    fun setUniform(uniformName: String, value: Vector4f) {
        uniforms[uniformName]?.let { glUniform4f(it, value.x, value.y, value.z, value.w) }
    }

    fun setUniform(uniformName: String, pointLight: PointLight) {
        setUniform("$uniformName.color", pointLight.color)
        setUniform("$uniformName.position", pointLight.position)
        setUniform("$uniformName.intensity", pointLight.intensity)
        val att = pointLight.attenuation
        setUniform("$uniformName.att.constant", att.constant)
        setUniform("$uniformName.att.linear", att.linear)
        setUniform("$uniformName.att.exponent", att.exponent)
    }

    fun setUniform(uniformName: String, material: Material) {
        setUniform("$uniformName.ambient", material.ambientColor)
        setUniform("$uniformName.diffuse", material.diffuseColor)
        setUniform("$uniformName.specular", material.specularColor)
        setUniform("$uniformName.hasTexture", if (material.isTextured) 1 else 0)
        setUniform("$uniformName.reflectance", material.reflectance)
    }

    fun setUniform(uniformName: String, value: Int) {
        uniforms[uniformName]?.let { glUniform1i(it, value) }
    }

    fun setUniform(uniformName: String, value: Float) {
        uniforms[uniformName]?.let { glUniform1f(it, value) }
    }

    fun link() {
        glLinkProgram(programId)
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024))
        }
        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId)
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId)
        }
        glValidateProgram(programId)
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024))
        }

    }

    fun bind() {
        glUseProgram(programId)
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun cleanup() {
        unbind()
        if (programId != 0) {
            glDeleteProgram(programId)
        }
    }
}