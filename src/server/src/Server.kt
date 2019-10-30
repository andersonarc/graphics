import Consts.PORT
import java.net.ServerSocket
import java.util.*

class Server {
    private val clients = LinkedList<Client>()
    private val socket = ServerSocket(PORT)

    init {
        while (true) {
            val client = Client(socket.accept())
            clients.forEach {
                client.write(it.latest)
            }
            clients.add(client)
            Thread {
                try {
                    while (true) {
                        val read = client.reader.readLine()
                        client.latest = read
                        clients.forEach {
                            if (it != client) {
                                it.write(read)
                            }
                        }
                    }
                } finally {
                    clients.remove(client)
                }
            }.start()
        }
    }
}