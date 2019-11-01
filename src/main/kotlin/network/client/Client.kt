package network.client

import graphics.data.objects.Object
import graphics.data.objects.ObjectData
import org.joml.Vector3f
import java.net.Socket

class Client(ip: String, port: Int) {
    private val server = Socket(ip, port)
    private val outputStream = server.getOutputStream()
    private val outputStreamWriter = outputStream.bufferedWriter()
    private val inputStream = server.getInputStream()
    private val inputStreamReader = inputStream.bufferedReader()

    fun write(obj: Object) {
        outputStreamWriter.write(obj.toString())
        outputStreamWriter.newLine()
        outputStreamWriter.flush()
    }

    fun read(): ObjectData {
        val read = inputStreamReader.readLine()
        var pos: Vector3f? = null
        var rot: Vector3f? = null
        var scale = 0f
        if (read != null) {
            val split = read.split(" ").toTypedArray()
            if (split.size != 3) {
                throw Exception("Something went wrong. Received: $read. Split to: ${split.contentToString()}")
            }
            split[0] = split[0].substring(1)
            split[2] = split[2].replace("]", "")

            for (i in split.indices) {
                if (i < 2) {
                    val subSplit = split[i].split(":")
                    when (i) {
                        0 -> pos = Vector3f(subSplit[0].toFloat(), subSplit[1].toFloat(), subSplit[2].toFloat())
                        1 -> rot = Vector3f(subSplit[0].toFloat(), subSplit[1].toFloat(), subSplit[2].toFloat())
                    }
                } else if (i == 3) {
                    scale = split[i].toFloat() / 100
                }
            }
        }
        return ObjectData(pos!!, rot!!, scale)
    }
}