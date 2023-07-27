package org.projectmct.nbeditor.nbtutils

open class NbtTreeVisitor {
  open fun <T> visit(tree: NbtTree.NbtTreeNode<T>){
    when (tree) {
      is NbtTree.NbtCompound -> visitCombination(tree)
      is NbtTree.NbtList -> visitArray(tree)
      is NbtTree.NbtLabel -> visitLabel(tree)
      is NbtTree.NbtByte -> visitByte(tree)
      is NbtTree.NbtShort -> visitShort(tree)
      is NbtTree.NbtInt -> visitInt(tree)
      is NbtTree.NbtLong -> visitLong(tree)
      is NbtTree.NbtFloat -> visitFloat(tree)
      is NbtTree.NbtDouble -> visitDouble(tree)
      is NbtTree.NbtBoolean -> visitBoolean(tree)

      else -> throw IllegalArgumentException("unknown node type \"${tree::class}\"")
    }
  }

  open fun visitCombination(comb: NbtTree.NbtCompound){
    for (entry in comb) {
      visitAttribute(entry.key, entry.value)
    }
  }

  open fun visitAttribute(name: String, value: NbtTree.NbtTreeNode<Any>){
    visit(value)
  }

  open fun visitArray(arr: NbtTree.NbtList){
    for (node in arr) {
      visit(node)
    }
  }

  open fun visitLabel(label: NbtTree.NbtLabel){}
  open fun visitByte(byte: NbtTree.NbtByte){}
  open fun visitShort(short: NbtTree.NbtShort){}
  open fun visitInt(int: NbtTree.NbtInt){}
  open fun visitLong(long: NbtTree.NbtLong){}
  open fun visitFloat(float: NbtTree.NbtFloat){}
  open fun visitDouble(double: NbtTree.NbtDouble){}
  open fun visitBoolean(bool: NbtTree.NbtBoolean){}
}