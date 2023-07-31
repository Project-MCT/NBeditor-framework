package org.projectmct.nbeditor.utils.nbt.nodes

import kotlin.reflect.KClass

abstract class NbtArray<Type: Any>: NbtTreeNode<MutableList<Type>>(), Iterable<Type>{
  init { value = ArrayList(); }

  abstract val elemType: KClass<*>

  fun set(index: Int, value: Type){
    checkType(value)
    value()[index] = value
  }
  fun get(index: Int): Type = value()[index]
  fun append(value: Type){
    checkType(value)
    value().add(value)
  }
  fun remove(index: Int) : Type = value().removeAt(index)
  val length: Int get() = value().size

  override fun iterator(): Iterator<Type> = value().iterator()

  override fun toString(): String {
    val builder = StringBuilder("[")

    var first = true
    value().forEach {
      if (!first) builder.append(",")
      first = false
      builder.append("$it")
    }

    return builder.append("]").toString()
  }

  @Suppress("UNCHECKED_CAST")
  protected fun copyTo(target: NbtArray<Type>): NbtArray<Type> {
    for (node in this) {
      target.append(if (node is NbtTreeNode<*>) {node.clone() as Type} else node)
    }

    return target
  }

  private fun checkType(target: Any){
    if (elemType == NbtEndMark::class)
      throw IllegalStateException("list element type cannot be end mark")

    if (target == NbtEndMark::class)
      throw IllegalArgumentException("end tag was invalid in list!")

    if (elemType != target::class)
      throw IllegalArgumentException("array type \"$elemType\", but handling a value with type: \"${target::class}\"")
  }

}