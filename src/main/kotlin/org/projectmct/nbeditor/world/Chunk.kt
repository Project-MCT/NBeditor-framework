package org.projectmct.nbeditor.world

import org.projectmct.nbeditor.utils.Point
import org.projectmct.nbeditor.utils.nbt.NbtTree
import java.io.File

abstract class Chunk(val sourceFile: File) {
  val nbtTag: NbtTree = NbtTree()

  abstract fun getBlock(x: Int, y: Int, z: Int): Block
  fun getBlock(pos: Point): Block = getBlock(pos.x, pos.y, pos.z)
  abstract fun setBlock(x: Int, y: Int, z: Int, block: Block)
  fun setBlock(pos: Point, block: Block) = setBlock(pos.x, pos.y, pos.z, block)

  abstract fun getEntityBlocks(): List<Block>
  abstract fun getEntities(): List<Entity>
}