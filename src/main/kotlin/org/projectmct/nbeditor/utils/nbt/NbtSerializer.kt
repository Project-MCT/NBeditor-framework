package org.projectmct.nbeditor.utils.nbt

import org.projectmct.nbeditor.utils.nbt.nodes.NbtTreeNode

interface NbtSerializer {
  fun write(tree: NbtTree)
  fun writeRaw(node: NbtTreeNode<*>)
}