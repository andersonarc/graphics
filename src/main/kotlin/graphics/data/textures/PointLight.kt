package graphics.data.textures

import org.joml.Vector3f


data class PointLight(var color: Vector3f, var position: Vector3f, var intensity: Float) {
    var attenuation = Attenuation(1f, 0f, 0f)

    constructor(color: Vector3f, position: Vector3f, intensity: Float, attenuation: Attenuation) : this(
        color, position, intensity
    ) {
        this.attenuation = attenuation
    }

    constructor(pointLight: PointLight) : this(
        Vector3f(pointLight.color), Vector3f(pointLight.position),
        pointLight.intensity, pointLight.attenuation
    )

    data class Attenuation(var constant: Float, var linear: Float, var exponent: Float)
}