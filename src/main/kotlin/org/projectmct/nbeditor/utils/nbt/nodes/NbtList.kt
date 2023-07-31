package org.projectmct.nbeditor.utils.nbt.nodes

import kotlin.reflect.KClass

class NbtList<Ty: NbtTreeNode<*>>(override var elemType: KClass<*>) : NbtArray<Ty>() {
  companion object{
    inline fun <reified Node: NbtTreeNode<*>> makeList(): NbtList<Node> = NbtList(Node::class)
    const val ID: Byte = 9
  }

  init {
    if (elemType == NbtByte::class || elemType == NbtInt::class || elemType == NbtLong::class)
      throw IllegalArgumentException("Integer types use an array of the corresponding type instead of a list of tag")
  }

  override fun typeID() = ID
  override fun clone() = copyTo(NbtList(elemType)) as NbtList
}