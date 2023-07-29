package org.projectmct.nbeditor.core

import java.util.concurrent.ThreadLocalRandom

open class QueuedTask(val task: Runnable) {
  /**任务当前状态*/
  var status: TaskStatus = TaskStatus.Queuing
  var time: Long = 0

  var ownerThread: Thread? = null

  open fun execute(){
    status = TaskStatus.Executing
    time = System.nanoTime();
    ownerThread = Thread.currentThread()
    task.run()
    finish()
  }
  open fun finish(){
    time = System.nanoTime()
    status = TaskStatus.Finished
  }
  fun consumed() = if (status == TaskStatus.Queuing) 0 else if (status == TaskStatus.Finished) time else System.nanoTime() - time

  enum class TaskStatus {
    Queuing,
    Executing,
    Finished
  }
}