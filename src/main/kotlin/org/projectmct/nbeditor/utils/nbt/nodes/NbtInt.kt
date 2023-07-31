package org.projectmct.nbeditor.utils.nbt.nodes

class NbtInt(int: Int): NbtTreeNode<Int>(){
  init { value = int; }

  companion object { const val ID: Byte = 3 }

  override fun typeID() = ID
  override fun clone() = NbtInt(value())
}