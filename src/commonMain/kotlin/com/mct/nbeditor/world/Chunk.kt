package com.mct.nbeditor.world

import com.mct.nbeditor.nbtutils.NbtTree
import com.mct.nbeditor.utils.File

interface Chunk {
  val nbtTag: NbtTree
  val sourceFile: File
}