package org.projectmct.nbeditor.world

import org.projectmct.nbeditor.utils.Point
import org.projectmct.nbeditor.utils.nbt.NbtTree

/**对一个方块的信息标记，用于保存方块数据，请注意，此结构没有映射关系，若您修改了一个方块的data，请使用 [Chunk.setBlock] 进行回置*/
class Block {
  val namespace: String
  val id: String
  val metdata: Byte

  val position: Point

  val data: NbtTree?

  constructor(pos: Point, namespace: String, id: String, metdata: Byte = 0, data: NbtTree? = null) {
    this.position = pos
    this.namespace = namespace
    this.id = id
    this.metdata = metdata
    this.data = data
  }

  constructor(pos: Point, name: String, metdata: Byte = 0, data: NbtTree? = null) {
    val splits = name.split(":")

    position = pos
    namespace = splits[0]
    id = splits[1]
    this.metdata = metdata
    this.data = data
  }

  fun isBlockEntity(): Boolean = data != null
}