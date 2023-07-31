import org.projectmct.nbeditor.Core
import org.projectmct.nbeditor.core.AbstractModule
import org.projectmct.nbeditor.utils.nbt.NbtTree
import org.projectmct.nbeditor.utils.nbt.nodes.*
import java.util.*

val tree = NbtTree()

val core = object: Core(){
  override fun init() {
    println("initializing")
    val root = NbtCompound()
    root["key1"] = NbtLabel("uuuu")
    root["key2"] = NbtLabel("hhhh")
    root["key3"] = NbtInt(15)
    val child = NbtCompound()
    root["key4"] = child
    child["k1"] = NbtLong(98L)
    child["k2"] = NbtLong(777L)
    val arr = NbtByteArray()
    child["k3"] = arr
    arr.append(17)
    arr.append(22)

    tree.set("", root)

    println(tree.toSNbt())
    val copy = NbtTree("", child.clone())

    println(NbtTree("", child).toSNbt())
    println(copy.toSNbt())
  }

  override fun mainLoop() {
  }

}

fun main() {
  core.maxUpdateSpeed = 60
  core.addModule(object: AbstractModule(){
    val scanner = Scanner(System.`in`)

    init{
      maxUpdateSpeed = 60
    }

    override fun initialize() {
      println("init cmd line")
    }

    override fun update() {
      val input = scanner.nextLine()

      for (i in 0..20){
        core.queueTask{
          println("$input: $i")
        }
      }
    }
  }, async = true)

  core.launch()
}
