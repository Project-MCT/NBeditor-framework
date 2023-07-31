import net.querz.nbt.io.NBTDeserializer
import org.projectmct.nbeditor.utils.nbt.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.GZIPInputStream

fun main() {
  val reader = SNbtParser(
    """
    "root": {
      "key2": [B; 123b, 111b, 319b],
      "key3": [I;
        1,2,3,4,5,6
      ]
    }
    """)

  val tree = reader.readNbt()
  println(tree.toSNbt())

  val writer = NbtWriter(FileOutputStream("output.nbt"))
  writer.write(tree)

  val r = NbtReader(FileInputStream("output.nbt"))
  println(r.readNbt().toSNbt())
}