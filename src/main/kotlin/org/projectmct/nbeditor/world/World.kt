package org.projectmct.nbeditor.world

import org.projectmct.nbeditor.utils.Point2D
import org.projectmct.nbeditor.utils.nbt.NbtTree
import java.io.File

abstract class World(val worldFile: File) {
  companion object{
    private const val CHUNK_WIDTH: Int = 16
    fun posToIndexX(posX: Int): Int = posX/CHUNK_WIDTH
    fun posToIndexY(posY: Int): Int = posY/CHUNK_WIDTH
  }

  val levelDat = NbtTree()
  val activityChunks: HashMap<Point2D, Chunk> = HashMap()

  fun icon() = File(worldFile, "icon.png")
  fun levelDatFile() = File(worldFile, "level.dat")

  abstract fun listAllFiles(): List<File>

  abstract fun getChunks(beginIndex: Point2D, endIndex: Point2D): Chunk
  fun getChunk(index: Point2D): Chunk = activityChunks.computeIfAbsent(index) { readChunkWithIndex(it.x, it.y) }
  fun freeChunk(chunk: Chunk, saveFile: Boolean = false){
    activityChunks.remove(chunk.chunkIndex)
    if (saveFile) chunk.save()
  }

  abstract fun readChunkWithIndex(indexX: Int, indexY: Int): Chunk
  abstract fun name(): String
  abstract fun seed(): Long
  abstract fun target(): String
  abstract fun version(): String

  abstract fun saveChangeToFile()
}