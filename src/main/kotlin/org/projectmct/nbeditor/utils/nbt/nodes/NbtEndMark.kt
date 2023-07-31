package org.projectmct.nbeditor.utils.nbt.nodes

class NbtEndMark private constructor() : NbtTreeNode<Unit>() {
  companion object{
    val INSTANCE = NbtEndMark()
    const val ID: Byte = 0
  }

  override fun toString() = ""
  override fun typeID() = ID

  override fun clone() = INSTANCE
}