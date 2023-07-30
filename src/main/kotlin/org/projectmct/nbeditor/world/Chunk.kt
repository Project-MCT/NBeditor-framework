package org.projectmct.nbeditor.world

import org.projectmct.nbeditor.utils.Point
import org.projectmct.nbeditor.utils.Point2D
import org.projectmct.nbeditor.utils.nbt.NbtTree
import java.io.File

abstract class Chunk(val sourceFile: File, val chunkIndex: Point2D) {
  val rawData: NbtTree = NbtTree()

  fun getBlock(pos: Point): Block = getBlock(pos.x, pos.y, pos.z)
  fun setBlock(pos: Point, block: Block) = setBlock(pos.x, pos.y, pos.z, block)

  /**Obtain a block information mark from this chunk. Note that the coordinates are relative coordinates within the block.
   * If the chunk is regarded as a vertical cuboid, the origin of the coordinates is located in the lower left corner, which is the vertex with the smallest coordinate number.
   *
   * @param x relative x coordinate
   * @param y relative y coordinate
   * @param z relative z coordinate*/
  abstract fun getBlock(x: Int, y: Int, z: Int): Block
  /**Set a block in this chunk. Note that the coordinates are relative coordinates within the block.
   * If the chunk is regarded as a vertical cuboid, the origin of the coordinates is at the lower left corner, which is the vertex with the smallest coordinate number.
   *
   * @param x relative x coordinate
   * @param y relative y coordinate
   * @param z relative z coordinate
   * @param block set block information*/
  abstract fun setBlock(x: Int, y: Int, z: Int, block: Block)

  /**Get all block entities in this chunk*/
  abstract fun getEntityBlocks(): List<Block>
  /**Get all entity information in this chunk*/
  abstract fun getEntities(): List<Entity>
  /**Save block changes to file*/
  abstract fun save()
}