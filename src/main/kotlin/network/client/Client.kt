package network.client

import graphics.data.objects.Mesh
import graphics.data.objects.Object
import graphics.data.objects.ObjectData
import graphics.data.textures.Texture
import launcher.Settings
import org.joml.Vector3f
import java.net.Socket
import java.net.SocketTimeoutException

class Client(ip: String, port: Int) {
    private val server: Socket = Socket(ip, port)
    private val writer = server.getOutputStream().bufferedWriter()
    private val reader = server.getInputStream().bufferedReader()

    init {
        server.soTimeout = Settings.TIMEOUT
    }

    fun write(obj: Object) {
        writer.write(obj.toString())
        writer.newLine()
        writer.flush()
    }

    fun read(mesh: Mesh, texture: Texture): Array<ObjectData>? {
        val read: String
        try {
            read = reader.readLine()
        } catch (e: SocketTimeoutException) {
            return null
        }
        val split = read.split("|")
        when (split.isEmpty()) {
            true -> return arrayOf(parse(read, mesh, texture))
        }
        return Array(split.size) { parse(split[it], mesh, texture) }
    }

    private fun parse(data: String, mesh: Mesh, texture: Texture): ObjectData {
        var id = 0
        var pos: Vector3f? = null
        var rot: Vector3f? = null
        var scale = 0f
        val split = data.split(" ").toTypedArray()
        for ((i, value) in split.withIndex()) {
            when (i) {
                0 -> id = value.toInt()
                1 -> pos = vector(value)
                2 -> rot = vector(value)
                3 -> scale = value.toFloat()
            }
        }
        return ObjectData(id, pos!!, rot!!, scale, mesh, texture)
    }

    private fun vector(data: String): Vector3f {
        val split = data.split(':')
        return Vector3f(split[0].toFloat(), split[1].toFloat(), split[2].toFloat())
    }
}