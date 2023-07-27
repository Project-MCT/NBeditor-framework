package com.mct.nbeditor.utils

interface QueuedTask {
  /**任务当前状态*/
  var status: TaskStatus

  fun execute()
  fun finish()
  fun consumed(): Long

  enum class TaskStatus {
    Queuing,
    Executing,
    Finished
  }
}

