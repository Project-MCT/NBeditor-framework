package org.projectmct.nbeditor.core

import kotlin.concurrent.thread

abstract class AbstractModule {
  /**the number of updates in one second normally, -1 meaning infinity*/
  var standardFrameSpeed: Int = -1

  private var lastTime: Long = System.nanoTime()
  private var delta: Long = 0

  fun loop(){
    delta = System.nanoTime() - lastTime
    lastTime = System.nanoTime()

    update()
  }

  fun lastTimeDelta(): Long{
    return delta
  }

  fun timeDelta() = delta;

  abstract fun initialize()
  abstract fun update()
}