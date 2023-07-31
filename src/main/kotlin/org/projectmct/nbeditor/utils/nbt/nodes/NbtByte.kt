package org.projectmct.nbeditor.utils.nbt.nodes

class NbtByte(byte: Byte): NbtTreeNode<Byte>(){
  init { value = byte; }

  companion object { const val ID: Byte = 1 }

  override fun toString() = value.toString() + "b"
  override fun typeID() = ID
  override fun clone() = NbtByte(value())
}