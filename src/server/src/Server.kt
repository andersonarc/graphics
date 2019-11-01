import Consts.PORT
import java.net.ServerSocket
import java.util.*

fun main(args: Array<String>) {
    val clients = LinkedList<Client>()
    val socket = ServerSocket(PORT)
    while (true) {
        val client = Client(socket.accept())
        clients.add(client)
        Thread {
            try {
                while (true) {
                    val read = client.read()
                    clients.forEach {
                        if (it != client) {
                            it.write(read)
                        }
                    }
                }
            } catch (e: Exception) {
                clients.remove(client)
            }
        }.start()
    }
}