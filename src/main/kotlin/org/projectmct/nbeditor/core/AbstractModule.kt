package org.projectmct.nbeditor.core

import org.projectmct.nbeditor.GLOBAL_STANDARD_FRAME_SPEED
import org.projectmct.nbeditor.SEC_NANO

abstract class AbstractModule {
  /**the number of updates in one second normally, -1 meaning infinity
   *
   * Only valid in asynchronous worker modules*/
  var maxUpdateSpeed: Int = -1

  private var lastTime: Long = System.nanoTime()
  private var delta: Float = 0f

  fun loop(){
    delta = (System.nanoTime() - lastTime).toFloat()/(SEC_NANO/GLOBAL_STANDARD_FRAME_SPEED)
    lastTime = System.nanoTime()

    update()
  }

  fun timeDelta(): Float = delta;

  abstract fun initialize()
  abstract fun update()
}