import java.net.Socket

class Client(socket: Socket) {
    private val reader = socket.getInputStream().bufferedReader()
    private val writer = socket.getOutputStream().bufferedWriter()

    fun write(string: String) {
        writer.write(string)
        writer.newLine()
        writer.flush()
    }

    fun read(): String {
        return reader.readLine()
    }
}