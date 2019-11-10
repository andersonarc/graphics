package graphics.data.textures

import org.lwjgl.opengl.ARBFramebufferObject.glGenerateMipmap
import org.lwjgl.opengl.GL11.*
import org.lwjgl.stb.STBImage.stbi_image_free
import org.lwjgl.stb.STBImage.stbi_load
import org.lwjgl.system.MemoryStack

class Texture(path: String) {
    val id: Int

    init {
        val stack = MemoryStack.stackPush()
        val w = stack.mallocInt(1)
        val h = stack.mallocInt(1)
        val channels = stack.mallocInt(1)
        val buffer = stbi_load(path, w, h, channels, 4)!!
        val width = w.get()
        val height = h.get()
        id = glGenTextures()

        glBindTexture(GL_TEXTURE_2D, id)
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
        glGenerateMipmap(GL_TEXTURE_2D)
        stbi_image_free(buffer)
    }

    fun cleanup() {
        glDeleteTextures(id)
    }
}