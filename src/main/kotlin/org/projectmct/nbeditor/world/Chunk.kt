package org.projectmct.nbeditor.world

import org.projectmct.nbeditor.nbtutils.NbtTree
import java.io.File

abstract class Chunk(val sourceFile: File) {
  val nbtTag: NbtTree = NbtTree()
}