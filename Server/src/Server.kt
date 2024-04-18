import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousServerSocketChannel
import kotlin.coroutines.suspendCoroutine

class Server(port: Int = 5106) {
    private val sSocket: AsynchronousServerSocketChannel =
        AsynchronousServerSocketChannel.open()

    private val serverScope = CoroutineScope(Dispatchers.IO)

    init {
        sSocket.bind(InetSocketAddress(port))
    }

    fun start() = serverScope.launch {
        val cSocket = suspendCoroutine {
            sSocket.accept(
                null,
                ActionCompletionHandler(it)
            )
        }
        var capacity = 4
        repeat(2) {
            val buf = ByteBuffer.allocate(capacity)
            val read = suspendCoroutine {
                cSocket.read(
                    buf,
                    null,
                    ActionCompletionHandler(it)
                )
            }

            buf.flip()
            println("Получено: $read байт.")
            if(it == 0) {
                capacity = buf.getInt()
                println(capacity)
            } else {
                println(buf.toString(Charsets.UTF_8))
            }
            buf.clear()
        }
        cSocket.close()
        sSocket.close()
    }
}