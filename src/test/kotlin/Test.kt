import org.projectmct.nbeditor.Core
import org.projectmct.nbeditor.core.AbstractModule
import org.projectmct.nbeditor.utils.nbt.NbtTree
import java.util.*

val tree = NbtTree()

val core = object: Core(){
  override fun init() {
    println("initializing")
    val root = NbtTree.NbtCompound()
    root["key1"] = NbtTree.NbtLabel("uuuu")
    root["key2"] = NbtTree.NbtLabel("hhhh")
    root["key3"] = NbtTree.NbtInt(15)
    val child = NbtTree.NbtCompound()
    root["key4"] = child
    child["k1"] = NbtTree.NbtLong(98L)
    child["k2"] = NbtTree.NbtBoolean(false)
    val arr = NbtTree.NbtList()
    child["k3"] = arr
    arr.append(NbtTree.NbtLong(98L))
    arr.append(NbtTree.NbtLabel("ssas"))
    tree.set(root)

    println(tree.toSNbt())
    val copy = NbtTree(child.clone())

    copy.root()["jiji"] = NbtTree.NbtLabel("jiasjiajisjdijaj")

    println(NbtTree(child).toSNbt())
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
