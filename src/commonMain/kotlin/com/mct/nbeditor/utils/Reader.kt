package com.mct.nbeditor.utils

import platform.posix.FILE
import platform.posix.*

interface Reader {
  fun read(): Byte
  fun reads(buffer: Array<Byte>, reads: Int): Int
  fun readAllBytes(): Array<Byte>
  fun close()
  fun reset()
}