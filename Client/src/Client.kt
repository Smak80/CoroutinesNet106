import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import kotlin.coroutines.suspendCoroutine

class Client(val host: String = "localhost", val port: Int = 5106) {

    private val socket: AsynchronousSocketChannel =
        AsynchronousSocketChannel.open()

    private val clientScope = CoroutineScope(Dispatchers.IO)

    fun start() = clientScope.launch{
        try {
            suspendCoroutine<Void> {
                socket.connect(InetSocketAddress(host, port), null, ActionCompletionHandler(it))
            }
            val ba = "При!вет!!!!".toByteArray()
            val buf = ByteBuffer.allocate(ba.size+4)
            buf.putInt(ba.size)
            buf.put(ba)
            buf.flip()
            val wrote = suspendCoroutine {
                socket.write(buf, null, ActionCompletionHandler(it))
            }
            println("Wrote: $wrote bytes")
            socket.close()
        } catch (_: Throwable){}
    }

}