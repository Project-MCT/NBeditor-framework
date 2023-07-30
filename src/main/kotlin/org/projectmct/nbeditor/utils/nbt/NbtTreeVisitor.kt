package org.projectmct.nbeditor.utils.nbt

open class NbtTreeVisitor<Arg> {
  open fun visit(tree: NbtTree.NbtTreeNode<*>, arg: Arg? = null){
    when (tree) {
      is NbtTree.NbtCompound -> visitCombination(tree, arg)
      is NbtTree.NbtList<*> -> visitArray(tree, arg)
      is NbtTree.NbtLabel -> visitLabel(tree, arg)
      is NbtTree.NbtByte -> visitByte(tree, arg)
      is NbtTree.NbtShort -> visitShort(tree, arg)
      is NbtTree.NbtInt -> visitInt(tree, arg)
      is NbtTree.NbtLong -> visitLong(tree, arg)
      is NbtTree.NbtFloat -> visitFloat(tree, arg)
      is NbtTree.NbtDouble -> visitDouble(tree, arg)

      else -> throw IllegalArgumentException("unknown node type \"${tree::class}\"")
    }
  }

  open fun visitCombination(comb: NbtTree.NbtCompound, arg: Arg? = null){
    for (entry in comb) {
      visitAttribute(entry.key, entry.value, arg)
    }
  }

  open fun visitAttribute(name: String, value: NbtTree.NbtTreeNode<*>, arg: Arg? = null){
    visit(value, arg)
  }

  open fun visitArray(arr: NbtTree.NbtList<*>, arg: Arg? = null){
    for (node in arr) {
      visit(node, arg)
    }
  }

  open fun visitLabel(label: NbtTree.NbtLabel, arg: Arg? = null){}
  open fun visitByte(byte: NbtTree.NbtByte, arg: Arg? = null){}
  open fun visitShort(short: NbtTree.NbtShort, arg: Arg? = null){}
  open fun visitInt(int: NbtTree.NbtInt, arg: Arg? = null){}
  open fun visitLong(long: NbtTree.NbtLong, arg: Arg? = null){}
  open fun visitFloat(float: NbtTree.NbtFloat, arg: Arg? = null){}
  open fun visitDouble(double: NbtTree.NbtDouble, arg: Arg? = null){}
}