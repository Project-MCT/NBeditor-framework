package com.mct.nbeditor.nbtutils

interface NbtTreeNode<Val> {
  fun getValue(): Val

  override fun toString(): String

  fun asLabel(): NbtLabel
  fun asByte(): NbtByte
  fun asShort(): NbtShort
  fun asInt(): NbtInt
  fun asLong(): NbtLong
  fun asFloat(): NbtFloat
  fun asDouble(): NbtDouble
  fun asArray(): NbtList
  fun asObject(): NbtCompound

  interface NbtLabel: NbtTreeNode<String>
  interface NbtByte: NbtTreeNode<Byte>
  interface NbtShort: NbtTreeNode<Short>
  interface NbtInt: NbtTreeNode<Int>
  interface NbtLong: NbtTreeNode<Long>
  interface NbtFloat: NbtTreeNode<Float>
  interface NbtDouble: NbtTreeNode<Double>
  interface NbtBoolean: NbtTreeNode<Boolean>

  interface NbtArray<T, Type: NbtTreeNode<T>>: NbtTreeNode<MutableList<Type>>, Iterable<Type> {
    fun set(index: Int, value: Type)
    fun get(index: Int): Type
    fun append(value: Type)
    fun remove(index: Int): Type
    override fun iterator(): Iterator<Type> { return getValue().iterator() }
  }
  interface NbtByteArray: NbtArray<Byte, NbtByte>
  interface NbtIntArray: NbtArray<Int, NbtInt>
  interface NbtLongArray: NbtArray<Long, NbtLong>
  interface NbtList: NbtArray<Any, NbtTreeNode<Any>>

  interface NbtCompound: NbtTreeNode<MutableMap<String, NbtTreeNode<Any>>>, Iterable<Map.Entry<String, NbtTreeNode<Any>>> {
    fun set(key: String, value: NbtTreeNode<Any>)
    fun get(key: String): NbtTreeNode<Any>
    fun remove(key: String): NbtTreeNode<Any>
    override fun iterator(): Iterator<Map.Entry<String, NbtTreeNode<Any>>>{
      return getValue().iterator()
    }
  }
}