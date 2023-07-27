package org.projectmct.nbeditor

import org.projectmct.nbeditor.core.AbstractModule
import org.projectmct.nbeditor.core.QueuedTask
import kotlin.concurrent.thread

private const val SEC_NANO = 1000000000

abstract class Core {
  /**the number of updates in one second normally, -1 meaning infinity*/
  var standardFrameSpeed: Int = -1
  var timeDelta: Long = 0

  private val syncModules: ArrayList<AbstractModule> = ArrayList()
  private val asyncModules: LinkedHashMap<AbstractModule, Thread> = LinkedHashMap()

  private val taskQueue: ArrayList<QueuedTask> = ArrayList()

  fun launch(){
    syncModules.forEach{ it.initialize() }
    asyncModules.forEach{ it.key.initialize() }
    init()

    var lastTime = System.nanoTime()

    while (true){
      timeDelta = System.nanoTime() - lastTime
      lastTime = System.nanoTime()

      syncModules.forEach{ it.loop() }
      mainLoop()

      val waitTime = System.nanoTime() + (SEC_NANO/standardFrameSpeed - timeDelta).minus(0)
      while (System.nanoTime() < waitTime) Thread.onSpinWait()//精度优先，此处执行自旋等待
    }
  }

  fun addModule(module: AbstractModule, async: Boolean, init: Boolean = false){
    if (init) module.initialize()
    if (async){
      asyncModules[module] = thread {
        while (true){
          module.loop()
          val wait = (SEC_NANO/module.standardFrameSpeed - module.timeDelta()).minus(0)
          if (wait > 0) Thread.sleep(wait/1000)//nanosecond to millisecond
        }
      }
    }
    else{
      syncModules.add(module)
    }
  }

  fun queueTask(task: Runnable): QueuedTask{
    val t = QueuedTask(task)
    taskQueue.add(t)
    return t
  }
  fun queuedTasks(): Int{
    return taskQueue.size;
  }

  abstract fun init()
  abstract fun mainLoop()
}