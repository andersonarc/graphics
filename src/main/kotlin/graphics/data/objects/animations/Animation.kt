package graphics.data.objects.animations

class Animation(
    val frames: List<AnimatedFrame>,
    val name: String,
    val duration: Double
) {
    var currentFrame: Int = 0

    fun getNextFrame(): AnimatedFrame {
        nextFrame()
        return this.frames[currentFrame]
    }

    fun nextFrame() {
        currentFrame = if (currentFrame + 1 > frames.size - 1) {
            0
        } else {
            currentFrame + 1
        }
    }
}