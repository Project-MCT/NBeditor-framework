package org.projectmct.nbeditor

import kotlinx.coroutines.Runnable
import org.projectmct.nbeditor.core.AbstractModule
import org.projectmct.nbeditor.core.QueuedTask
import java.util.*
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap
import kotlin.concurrent.thread

const val SEC_NANO: Long = 1000000000
/**global standard unit frame rate, is base value for frame increment calculation*/
const val GLOBAL_STANDARD_FRAME_SPEED = 60

abstract class Core {
  /**the number of updates in one second normally, -1 meaning infinity*/
  var maxUpdateSpeed: Int = -1
  /**the maximum number of asynchronous tasks that can be executed simultaneously, please modify before initialize*/
  var taskExecutes: Int = 8

  private var timeDelta: Float = 0f

  private val syncModules: ArrayList<AbstractModule> = ArrayList()
  private val asyncModules: LinkedHashMap<AbstractModule, Thread> = LinkedHashMap()

  private val taskMap: MutableMap<QueuedTask, Runnable> = Collections.synchronizedMap(HashMap())
  private var queuingTasks = 0
  private var executingTasks = 0

  private var executor: ThreadPoolExecutor? = null

  fun timeDelta() = timeDelta
  fun queuedTasks() = queuingTasks
  fun executingTasks() = executingTasks

  /**the entry point required by the program, this method is called directly at startup*/
  fun launch(){
    syncModules.forEach{ it.initialize() }
    asyncModules.forEach{ it.key.initialize() }
    init()

    executor = ThreadPoolExecutor(taskExecutes, taskExecutes, SEC_NANO, TimeUnit.NANOSECONDS, LinkedBlockingDeque())

    var lastTime = System.nanoTime()

    while (true){
      val curr = System.nanoTime()

      timeDelta = (curr - lastTime).toFloat()/(SEC_NANO/GLOBAL_STANDARD_FRAME_SPEED)
      lastTime = curr
      syncModules.forEach{ it.loop() }
      mainLoop()

      val wait = (SEC_NANO/maxUpdateSpeed - System.nanoTime() + curr).minus(0)
      if (wait > 0) Thread.sleep(wait/1000000, (wait%1000000).toInt())
    }
  }

  /**Add synchronous or asynchronous work modules to the core
   *
   * @param module work module that added
   * @param async Whether the module is asynchronously refreshed
   * @param init Whether to initialize the module immediately, please avoid performing immediate initialization before startup,
   *             which will cause the module to be initialized repeatably*/
  fun addModule(module: AbstractModule, async: Boolean = false, init: Boolean = false){
    if (init) module.initialize()
    if (async){
      asyncModules[module] = thread {
        while (true){
          val last = System.nanoTime()
          module.loop()
          val wait = (SEC_NANO/module.maxUpdateSpeed - System.nanoTime() + last).minus(0)
          if (wait > 0) Thread.sleep(wait/1000000, (wait%1000000).toInt())
        }
      }
    }
    else{
      syncModules.add(module)
    }
  }

  /**Remove a working module from the core
   *
   * @param module The work module that removed*/
  fun removeModule(module: AbstractModule){
    syncModules.remove(module)
    asyncModules.remove(module)?.interrupt()
  }

  fun queueTask(task: Runnable): QueuedTask{
    val t = object: QueuedTask(task){
      override fun finish() {
        synchronized(this@Core){
          (executor?: throw IllegalStateException("core uninitialized!")).remove(taskMap[this])
          super.finish()
          if (status == TaskStatus.Queuing) queuingTasks--
          else executingTasks--
        }
      }
    }

    queuingTasks++
    val exe = Runnable {
      queuingTasks--
      executingTasks++
      t.execute()
      executingTasks--
    }
    taskMap[t] = exe
    executor?.execute(exe)
    return t
  }

  abstract fun init()
  abstract fun mainLoop()
}