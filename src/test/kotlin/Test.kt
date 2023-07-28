import org.projectmct.nbeditor.Core
import org.projectmct.nbeditor.core.AbstractModule
import java.util.Scanner

val core = object: Core(){
  override fun init() {
    println("initializing")
  }

  override fun mainLoop() {
    println(timeDelta())
  }

}

fun main() {
  core.standardFrameSpeed = 1
  core.addModule(object: AbstractModule(){
    val scanner = Scanner(System.`in`)

    init{
      standardFrameSpeed = 60
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
