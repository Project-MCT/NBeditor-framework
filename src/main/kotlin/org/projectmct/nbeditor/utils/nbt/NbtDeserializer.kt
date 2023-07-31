package org.projectmct.nbeditor.utils.nbt

import org.projectmct.nbeditor.utils.nbt.nodes.NbtCompound
import org.projectmct.nbeditor.utils.nbt.nodes.NbtTreeNode

interface NbtDeserializer {
  fun readNbt(): NbtTree
  fun readRaw(): NbtTreeNode<*>
  fun readToTree(tree: NbtTree){
    tree.set(readNbt())
  }
}