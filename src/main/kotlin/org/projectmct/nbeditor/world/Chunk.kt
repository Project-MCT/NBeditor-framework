package org.projectmct.nbeditor.world

import org.projectmct.nbeditor.utils.Point
import org.projectmct.nbeditor.utils.Point2D
import org.projectmct.nbeditor.utils.nbt.NbtTree
import java.io.File

abstract class Chunk(val sourceFile: File, val chunkIndex: Point2D) {
  val nbtTag: NbtTree = NbtTree()

  fun getBlock(pos: Point): Block = getBlock(pos.x, pos.y, pos.z)
  fun setBlock(pos: Point, block: Block) = setBlock(pos.x, pos.y, pos.z, block)

  abstract fun getBlock(x: Int, y: Int, z: Int): Block
  abstract fun setBlock(x: Int, y: Int, z: Int, block: Block)

  abstract fun getEntityBlocks(): List<Block>
  abstract fun getEntities(): List<Entity>
  abstract fun save()
}