import java.net.Socket

class Client(socket: Socket) {
    val reader = socket.getInputStream().bufferedReader()
    val writer = socket.getOutputStream().bufferedWriter()
    var latest = ""

    fun write(string: String) {
        writer.write(string)
        writer.newLine()
        writer.flush()
    }

    fun read(): String {
        return reader.readLine()
    }
}