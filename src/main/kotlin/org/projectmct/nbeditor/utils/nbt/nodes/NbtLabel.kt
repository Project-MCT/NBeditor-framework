package org.projectmct.nbeditor.utils.nbt.nodes

class NbtLabel(string: String) : NbtTreeNode<String>(){
  init { value = string; }

  companion object { const val ID: Byte = 8 }

  override fun toString() = "\"${value()}\""
  override fun typeID() = ID
  override fun clone() = NbtLabel(value())
}