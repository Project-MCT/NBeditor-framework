package org.projectmct.nbeditor.world

import org.projectmct.nbeditor.utils.Point
import org.projectmct.nbeditor.utils.nbt.NbtTree
import java.util.Date

/**对一个方块的信息标记，用于保存方块数据，请注意，此结构没有映射关系，若您修改了一个方块的信息，请使用 [Chunk.setBlock] 进行回置否则不会产生实际效果*/
class Block(pos: Point, val name: String, val biomes: String, val meta: Byte = 0, val entityData: NbtTree? = null) {
  val position: Point = pos

  fun isBlockEntity(): Boolean = entityData != null
}