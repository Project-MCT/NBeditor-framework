import net.querz.nbt.io.LittleEndianNBTInputStream
import net.querz.nbt.io.NBTDeserializer
import net.querz.nbt.io.NBTInputStream
import org.projectmct.nbeditor.utils.nbt.NbtReader
import org.projectmct.nbeditor.utils.nbt.NbtTree
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Scanner
import java.util.zip.GZIPInputStream

fun main() {
  val input = GZIPInputStream(FileInputStream("level.dat"))

  val n = NbtTree()
  NbtReader(input).readToTree(n)

  println(n.toSNbt())

  val scan = Scanner(System.`in`)
  while (scan.nextLine() != null){
    print(input.read())
  }

}