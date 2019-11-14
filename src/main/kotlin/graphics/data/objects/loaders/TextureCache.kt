package graphics.data.objects.loaders

import graphics.data.textures.Texture
import java.util.*


object TextureCache {
    private val textures = HashMap<String, Texture>()

    fun texture(path: String): Texture {
        var texture = textures[path]
        if (texture == null) {
            texture = Texture(path)
            textures[path] = texture
        }
        return texture
    }
}