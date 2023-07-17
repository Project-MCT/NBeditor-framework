package nbtutils

interface NbtTree{
    var nodes: Int
    var root: NbtTreeNode.NbtCombination?

    fun root(): NbtTreeNode.NbtCombination{
        if (root == null) throw IllegalStateException("this tree was not initialized yet!");
        return root as NbtTreeNode.NbtCombination
    }

    fun parseJSON(json: String)
    fun toJSON(): String

    @Suppress("UNCHECKED_CAST")
    fun accept(visitor: NbtTreeVisitor){
        visitor.visit(root() as NbtTreeNode<Any>)
    }
}