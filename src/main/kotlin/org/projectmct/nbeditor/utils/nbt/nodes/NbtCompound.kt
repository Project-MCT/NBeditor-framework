package org.projectmct.nbeditor.utils.nbt.nodes

class NbtCompound: NbtTreeNode<MutableMap<String, NbtTreeNode<*>>>(), Iterable<Map.Entry<String, NbtTreeNode<*>>>{
  init { value = LinkedHashMap(); }

  companion object { const val ID: Byte = 10 }

  operator fun <T> set(key: String, value: NbtTreeNode<T>) { value()[key] = value }
  fun get(key: String) = value()[key] ?: throw IllegalArgumentException("no such attribute named $key")
  fun remove(key: String) = value().remove(key)?: throw IllegalArgumentException("no such attribute named $key")

  override fun iterator() = value().iterator()
  override fun typeID() = ID

  override fun toString(): String {
    val builder = StringBuilder("{")

    var first = true
    value().forEach { entry ->
      if (!first) {
        builder.append(",")
      }
      first = false
      builder.append("\"${entry.key}\":${entry.value}")
    }

    return builder.append("}").toString()
  }

  override fun clone(): NbtCompound {
    val res = NbtCompound()

    for (entry in this) {
      res[entry.key] = entry.value.clone()
    }

    return res
  }
}