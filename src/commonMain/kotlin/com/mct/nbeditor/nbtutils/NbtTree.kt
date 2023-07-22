package com.mct.nbeditor.nbtutils

interface NbtTree{
    var nodes: Int
    var root: NbtTreeNode.NbtCompound?

    fun root(): NbtTreeNode.NbtCompound {
        if (root == null) throw IllegalStateException("this tree was not initialized yet!");
        return root as NbtTreeNode.NbtCompound
    }

    fun parseSNbt(json: String)
    fun toSNbt(): String

    @Suppress("UNCHECKED_CAST")
    fun accept(visitor: NbtTreeVisitor){
        visitor.visit(root() as NbtTreeNode<Any>)
    }
}