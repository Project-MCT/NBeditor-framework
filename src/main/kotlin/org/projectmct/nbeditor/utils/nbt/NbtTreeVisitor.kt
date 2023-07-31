package org.projectmct.nbeditor.utils.nbt

import org.projectmct.nbeditor.utils.nbt.nodes.*

open class NbtTreeVisitor<Arg> {
  fun visit(tree: NbtTreeNode<*>, arg: Arg? = null){
    when (tree) {
      is NbtCompound -> visitCompound(tree, arg)
      is NbtList<*> -> visitList(tree, arg)

      is NbtLabel -> visitLabel(tree, arg)
      is NbtByte -> visitByte(tree, arg)
      is NbtShort -> visitShort(tree, arg)
      is NbtInt -> visitInt(tree, arg)
      is NbtLong -> visitLong(tree, arg)
      is NbtFloat -> visitFloat(tree, arg)
      is NbtDouble -> visitDouble(tree, arg)

      is NbtByteArray -> visitByteArray(tree, arg)
      is NbtIntArray -> visitIntArray(tree, arg)
      is NbtLongArray -> visitLongArray(tree, arg)

      else -> throw IllegalArgumentException("unknown node type \"${tree::class}\"")
    }
  }

  protected open fun visitCompound(compound: NbtCompound, arg: Arg? = null){
    for (entry in compound) {
      visitAttribute(entry.key, entry.value, arg)
    }
  }

  protected open fun visitAttribute(name: String, value: NbtTreeNode<*>, arg: Arg? = null){
    visit(value, arg)
  }
  protected open fun visitList(arr: NbtList<*>, arg: Arg? = null){
    for (node in arr) {
      visit(node, arg)
    }
  }

  protected open fun visitLabel(label: NbtLabel, arg: Arg? = null){}
  protected open fun visitByte(byte: NbtByte, arg: Arg? = null){}
  protected open fun visitShort(short: NbtShort, arg: Arg? = null){}
  protected open fun visitInt(int: NbtInt, arg: Arg? = null){}
  protected open fun visitLong(long: NbtLong, arg: Arg? = null){}
  protected open fun visitFloat(float: NbtFloat, arg: Arg? = null){}
  protected open fun visitDouble(double: NbtDouble, arg: Arg? = null){}

  protected open fun visitByteArray(byteArray: NbtByteArray, arg: Arg? = null){}
  protected open fun visitIntArray(intArray: NbtIntArray, arg: Arg? = null){}
  protected open fun visitLongArray(longArray: NbtLongArray, arg: Arg? = null){}
}