package org.projectmct.nbeditor.utils.nbt.nodes

class NbtFloat(float: Float): NbtTreeNode<Float>(){
  init { value = float; }

  companion object { const val ID: Byte = 5 }

  override fun toString() = formatter.format(value) + "f"
  override fun typeID() = ID
  override fun clone() = NbtFloat(value())
}