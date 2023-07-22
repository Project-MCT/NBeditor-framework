package com.mct.nbeditor

import com.mct.nbeditor.nbtutils.NbtGzipEncoder
import com.mct.nbeditor.utils.NbtFormatFactory
import com.mct.nbeditor.utils.QueuedTask

interface Core {
  var nbtEncoder: NbtGzipEncoder
  var formatter: NbtFormatFactory

  fun os(): String
  fun memUsed(): Long
  fun memTotal(): Long

  fun mainLoop()
  fun asyncTask(task: () -> Unit): QueuedTask
  fun listAsyncTasks(): Array<QueuedTask>
}