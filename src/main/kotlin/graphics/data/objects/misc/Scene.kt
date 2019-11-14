package graphics.data.objects.misc

import graphics.data.objects.Object
import graphics.data.objects.loaders.staticMesh

class Scene {
    val objects = ArrayList<Object>()
    lateinit var skyBox: SkyBox
    //todo: SceneLight

    fun init() {
        skyBox = SkyBox("skybox".staticMesh()[0])
        skyBox.scale = 100f
    }
}