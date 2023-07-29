package org.projectmct.nbeditor.world

import org.projectmct.nbeditor.utils.nbt.NbtTree
import java.io.File

abstract class World(val worldFile: File) {
  companion object{
    private const val CHUNK_WIDTH: Int = 16
    fun posToIndexX(posX: Int): Int = posX/CHUNK_WIDTH
    fun posToIndexY(posY: Int): Int = posY/CHUNK_WIDTH
  }

  val levelDat = NbtTree()

  fun icon() = File(worldFile, "icon.png")
  fun levelDatFile() = File(worldFile, "level.dat")

  abstract fun listAllFiles(): List<File>

  abstract fun getChunks(beginIndexX: Int, beginIndexY: Int, endIndexX: Int, endIndexY: Int): Chunk
  abstract fun getChunk(indexX: Int, indexY: Int): Chunk

  abstract fun name(): String
  abstract fun seed(): Long
  abstract fun target(): String
  abstract fun version(): String

  abstract fun saveChangeToFile()
}