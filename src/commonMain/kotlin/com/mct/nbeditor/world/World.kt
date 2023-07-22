package com.mct.nbeditor.world

import com.mct.nbeditor.nbtutils.NbtTree
import com.mct.nbeditor.utils.File

interface World {
  companion object{
    const val CHUNK_WIDTH: Int = 16
    inline fun posToIndexX(posX: Int): Int{ return posX/CHUNK_WIDTH }
    inline fun posToIndexY(posY: Int): Int{ return posY/CHUNK_WIDTH }
  }

  val worldFile: File
  val levelDat: NbtTree

  fun getChunks(beginIndexX: Int, beginIndexY: Int, endIndexX: Int, endIndexY: Int): Chunk
  fun getChunk(indexX: Int, indexY: Int): Chunk

  fun name(): String
  fun seed(): Long
  fun target(): String
  fun version(): String

  fun saveChangeToFile()
}