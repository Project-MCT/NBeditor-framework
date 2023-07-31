package org.projectmct.nbeditor.utils.nbt.nodes

class NbtShort(short: Short): NbtTreeNode<Short>(){
  init { value = short; }

  companion object { const val ID: Byte = 2 }

  override fun toString() = value.toString() + "s"
  override fun typeID() = ID
  override fun clone() = NbtShort(value())
}