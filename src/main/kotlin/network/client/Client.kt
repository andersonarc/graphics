package network.client

import graphics.data.objects.Mesh
import graphics.data.objects.Object
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

    fun read(mesh: Mesh): Object {
        val obj = Object(mesh)
        val read = inputStreamReader.readLine()
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
                        0 -> obj.setPosition(subSplit[0].toFloat(), subSplit[1].toFloat(), subSplit[2].toFloat())
                        1 -> obj.setRotation(subSplit[0].toFloat(), subSplit[1].toFloat(), subSplit[2].toFloat())
                    }
                } else if (i == 3) {
                    obj.scale = split[i].toFloat() / 100
                }
            }
        }
        return obj
    }
}