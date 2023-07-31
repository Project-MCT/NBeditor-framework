package org.projectmct.nbeditor.utils.nbt.nodes

import org.projectmct.nbeditor.utils.nbt.NbtTree

class NbtDouble(double: Double): NbtTreeNode<Double>(){
  init { value = double; }

  companion object { const val ID: Byte = 6 }

  override fun toString() = formatter.format(value) + "d"
  override fun typeID() = ID
  override fun clone() = NbtDouble(value())
}