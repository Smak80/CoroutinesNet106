import java.nio.ByteBuffer
import java.nio.charset.Charset

fun ByteBuffer.toString(charset: Charset) =
    charset.decode(this).toString()

fun String.toByteBuffer(charset: Charset = Charsets.UTF_8) =
    ByteBuffer.wrap(toByteArray(charset))