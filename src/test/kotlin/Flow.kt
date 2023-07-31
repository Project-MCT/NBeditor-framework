import java.io.FileInputStream
import java.io.StringReader
import java.util.Scanner
import java.util.zip.GZIPInputStream

fun main() {
  val reader = StringReader("123456789")

  var i = 5
  while(true){
    if (i == 3)reader.mark(0)
    val c = reader.read()
    if (c == -1) break

    println(c.toChar())
    i--
    if (i == 0) reader.reset()
  }
}