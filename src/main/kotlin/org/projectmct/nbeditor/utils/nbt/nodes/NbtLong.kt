package org.projectmct.nbeditor.utils.nbt.nodes

class NbtLong(long: Long): NbtTreeNode<Long>(){
  init { value = long; }

  companion object { const val ID: Byte = 4 }

  override fun toString() = value.toString() + "L"
  override fun typeID() = ID
  override fun clone() = NbtLong(value())
}