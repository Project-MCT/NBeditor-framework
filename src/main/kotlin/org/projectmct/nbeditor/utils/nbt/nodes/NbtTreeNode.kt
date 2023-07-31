package org.projectmct.nbeditor.utils.nbt.nodes

import java.text.NumberFormat

abstract class NbtTreeNode<Val>: Cloneable{
  companion object{
    internal val formatter = NumberFormat.getInstance()

    init {
      formatter.isGroupingUsed = false
      formatter.minimumFractionDigits = 1
      formatter.maximumFractionDigits = 8
    }
  }

  protected var value: Val? = null

  fun value(): Val = value?: throw IllegalStateException("null value!")
  override fun toString() = value.toString()

  abstract fun typeID(): Byte

  fun asLabel(): NbtLabel {
    if (this !is NbtLabel) throw ClassCastException("node is ${this::class}, can not be cast to Label"); return this
  }
  fun asByte(): NbtByte {
    if (this !is NbtByte) throw ClassCastException("node is ${this::class}, can not be cast to Byte"); return this
  }
  fun asShort(): NbtShort {
    if (this !is NbtShort) throw ClassCastException("node is ${this::class}, can not be cast to Short"); return this
  }
  fun asInt(): NbtInt {
    if (this !is NbtInt) throw ClassCastException("node is ${this::class}, can not be cast to Integer"); return this
  }
  fun asLong(): NbtLong {
    if (this !is NbtLong) throw ClassCastException("node is ${this::class}, can not be cast to Long"); return this
  }
  fun asFloat(): NbtFloat {
    if (this !is NbtFloat) throw ClassCastException("node is ${this::class}, can not be cast to Float"); return this
  }
  fun asDouble(): NbtDouble {
    if (this !is NbtDouble) throw ClassCastException("node is ${this::class}, can not be cast to Double"); return this
  }

  fun asByteArray(): NbtByteArray {
    if (this !is NbtByteArray) throw ClassCastException("node is ${this::class}, can not be cast to byte array"); return this
  }
  fun asIntArray(): NbtIntArray {
    if (this !is NbtIntArray) throw ClassCastException("node is ${this::class}, can not be cast to int array"); return this
  }
  fun asLongArray(): NbtLongArray {
    if (this !is NbtLongArray) throw ClassCastException("node is ${this::class}, can not be cast to long array"); return this
  }

  fun asList(): NbtList<*> {
    if (this !is NbtList<*>) throw ClassCastException("node is ${this::class}, can not be cast to List"); return this
  }
  fun asCompound(): NbtCompound {
    if (this !is NbtCompound) throw ClassCastException("node is ${this::class}, can not be cast to js object"); return this
  }

  public abstract override fun clone(): NbtTreeNode<Val>
}