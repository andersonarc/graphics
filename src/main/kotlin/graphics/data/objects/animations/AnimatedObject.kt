package graphics.data.objects.animations

import graphics.data.objects.Mesh
import graphics.data.objects.Object

class AnimatedObject(meshes: Array<Mesh>, animations: Map<String, Animation>) : Object(meshes) {
    private var currentAnimation: Animation?

    init {
        val entry = animations.entries.stream().findFirst()
        currentAnimation = if (entry.isPresent) entry.get().value else null
    }
}