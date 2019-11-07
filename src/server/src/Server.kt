import Consts.PORT
import Main.reading
import Main.writing
import java.net.ServerSocket
import java.util.*
import kotlin.collections.HashMap

object Main {
    @Volatile
    var reading = false
    @Volatile
    var writing = false
}

fun main(args: Array<String>) {
    val clients = LinkedList<Client>()
    val socket = ServerSocket(PORT)
    Thread {
        while (true) {
            val client = Client(socket.accept())
            writing = true
            while (true) {
                if (!reading) {
                    clients.add(client)
                    break
                }
            }
            writing = false
        }
    }.start()
    while (true) {
        if (!writing) {
            reading = true
            val remove = ArrayList<Client>()
            val map = HashMap<Client, String>()
            for (client in clients) {
                try {
                    map[client] = "|" + client.hashCode() + " " + client.read()
                } catch (e: Exception) {
                    remove.add(client)
                }
            }
            for (client in clients) {
                try {
                    val builder = StringBuilder()
                    for (entry in map) {
                        if (entry.component1() != client) {
                            builder.append(entry.component2())
                        }
                    }
                    if (builder.isNotEmpty()) {
                        builder.deleteCharAt(0)
                        client.write(builder.toString())
                    }
                } catch (e: Exception) {
                    remove.add(client)
                }
            }
            for (client in remove) {
                clients.remove(client)
            }
            reading = false
        }
    }
}