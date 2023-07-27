package com.mct.nbeditor.world

import com.mct.nbeditor.nbtutils.NbtTree
import java.io.File

abstract class Chunk(val sourceFile: File) {
  val nbtTag: NbtTree = NbtTree()
}