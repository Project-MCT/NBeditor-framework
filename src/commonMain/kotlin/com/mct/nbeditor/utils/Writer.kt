package com.mct.nbeditor.utils

import com.mct.nbeditor.world.World

interface Writer {
  fun write(byte: Byte)
  fun write(bytes: Array<Byte>, off: Int, len: Int)
  fun close()
  fun reset(){

  }
}