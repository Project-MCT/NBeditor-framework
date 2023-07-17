package nbtutils

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
  fun asArray(): NbtArray
  fun asObject(): NbtCombination

  interface NbtLabel: NbtTreeNode<String>
  interface NbtByte: NbtTreeNode<Byte>
  interface NbtShort: NbtTreeNode<Short>
  interface NbtInt: NbtTreeNode<Int>
  interface NbtLong: NbtTreeNode<Long>
  interface NbtFloat: NbtTreeNode<Float>
  interface NbtDouble: NbtTreeNode<Double>
  interface NbtBoolean: NbtTreeNode<Boolean>
  interface NbtArray: NbtTreeNode<MutableList<NbtTreeNode<Any>>>, Iterable<NbtTreeNode<Any>> {
    fun set(index: Int, value: NbtTreeNode<Any>)
    fun get(index: Int): NbtTreeNode<Any>
    fun append(value: NbtTreeNode<Any>)
    fun remove(index: Int): NbtTreeNode<Any>
    override fun iterator(): Iterator<NbtTreeNode<Any>>{ return getValue().iterator() }
  }
  interface NbtCombination: NbtTreeNode<MutableMap<String, NbtTreeNode<Any>>>, Iterable<Map.Entry<String, NbtTreeNode<Any>>> {
    fun set(key: String, value: NbtTreeNode<Any>)
    fun get(key: String): NbtTreeNode<Any>
    fun remove(key: String): NbtTreeNode<Any>
    override fun iterator(): Iterator<Map.Entry<String, NbtTreeNode<Any>>>{
      return getValue().iterator()
    }
  }
}