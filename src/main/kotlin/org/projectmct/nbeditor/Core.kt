package org.projectmct.nbeditor

import org.projectmct.nbeditor.core.AbstractModule
import org.projectmct.nbeditor.core.QueuedTask
import java.lang.IllegalStateException
import java.util.LinkedList
import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap
import kotlin.concurrent.thread

private const val SEC_NANO: Long = 1000000000

abstract class Core {
  /**the number of updates in one second normally, -1 meaning infinity*/
  var standardFrameSpeed: Int = -1
  /**the maximum number of asynchronous tasks that can be executed simultaneously, please modify before initialize*/
  var taskExecutes: Int = 8

  private var timeDelta: Long = 0

  private val syncModules: ArrayList<AbstractModule> = ArrayList()
  private val asyncModules: LinkedHashMap<AbstractModule, Thread> = LinkedHashMap()

  private val taskMap: HashMap<QueuedTask, Runnable> = HashMap()
  private val taskQueue: LinkedList<QueuedTask> = LinkedList()
  private var executingTasks = 0

  private var executor: ThreadPoolExecutor? = null

  /**last main loop refresh interval*/
  fun timeDelta(): Long = timeDelta

  /**the entry point required by the program, this method is called directly at startup*/
  fun launch(){
    syncModules.forEach{ it.initialize() }
    asyncModules.forEach{ it.key.initialize() }
    init()

    executor = ThreadPoolExecutor(taskExecutes, taskExecutes, SEC_NANO, TimeUnit.NANOSECONDS, LinkedBlockingDeque())
    thread {
      while (true) {
        synchronized(this){
          //更新异步任务队列
          while (executingTasks < taskExecutes && !taskQueue.isEmpty()) {
            val task = taskQueue.removeFirst()
            executingTasks++

            val exe = Runnable {
              task.execute()
              executingTasks--
            }
            taskMap[task] = exe
            executor?.execute(exe)
          }
        }
      }
    }

    var lastTime = System.nanoTime()

    while (true){
      timeDelta = System.nanoTime() - lastTime
      lastTime = System.nanoTime()

      syncModules.forEach{ it.loop() }
      mainLoop()

      val waitTime = System.nanoTime() + (SEC_NANO/standardFrameSpeed - System.nanoTime() + lastTime).minus(0)
      while (System.nanoTime() < waitTime) Thread.onSpinWait()//精度优先，此处执行自旋等待
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
          val wait = (SEC_NANO/module.standardFrameSpeed - System.nanoTime() + last).minus(0)
          if (wait > 0) Thread.sleep(wait/1000)//nanosecond to millisecond
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
    synchronized(this){
      val t = object: QueuedTask(task){
        override fun finish() {
          synchronized(this){
            (executor?: throw IllegalStateException("core uninitialized!")).remove(taskMap[this])
          }
          super.finish()
        }
      }
      taskQueue.add(t)
      return t
    }
  }
  fun queuedTasks(): Int{
    return taskQueue.size;
  }

  abstract fun init()
  abstract fun mainLoop()
}