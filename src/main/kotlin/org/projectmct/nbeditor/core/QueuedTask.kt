package org.projectmct.nbeditor.core

class QueuedTask(val task: Runnable) {
  /**任务当前状态*/
  var status: TaskStatus = TaskStatus.Queuing
  var time: Long = 0

  fun execute(){
    status = TaskStatus.Executing
    time = System.nanoTime();
    task.run()
    finish()
  }
  fun finish(){
    time = System.nanoTime()
    status = TaskStatus.Finished
  }
  fun consumed(): Long{
    return if (status == TaskStatus.Queuing) 0 else if (status == TaskStatus.Finished) time else System.nanoTime() - time
  }

  enum class TaskStatus {
    Queuing,
    Executing,
    Finished
  }
}