package org.projectmct.nbeditor.nbtutils

class NbtTree {
  private var root: NbtCompound? = null

  constructor()

  constructor(src: NbtCompound){
    root = src
  }

  fun root(): NbtCompound {
    return root?: throw IllegalStateException("this tree was not initialized")
  }

  fun parseSNbt(json: String) {
    TODO("Not yet implemented")
  }

  fun toSNbt(): String {
    return root.toString()
  }

  open class NbtTreeNode<Val>{
    protected var value: Val? = null

     fun value(): Val {
      return value?: throw IllegalStateException("null value!")
    }

     override fun toString(): String{
      return value.toString()
    }

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

     fun asArray(): NbtList {
      if (this !is NbtList) throw ClassCastException("node is ${this::class}, can not be cast to Array"); return this
    }
     fun asObject(): NbtCompound {
      if (this !is NbtCompound) throw ClassCastException("node is ${this::class}, can not be cast to js object"); return this
    }
  }

  class NbtLabel(string: String) : NbtTreeNode<String>(){
    init { value = string; }

    override fun toString(): String {
      return "\"${value()}\""
    }
  }

  class NbtByte(byte: Byte): NbtTreeNode<Byte>(){
    init { value = byte; }

    override fun toString(): String {
      return value.toString() + "b"
    }
  }

  class NbtShort(short: Short): NbtTreeNode<Short>(){
    init { value = short; }

    override fun toString(): String {
      return value.toString() + "s"
    }
  }

  class NbtInt(int: Int): NbtTreeNode<Int>(){
    init { value = int; }
  }

  class NbtLong(long: Long): NbtTreeNode<Long>(){
    init { value = long; }

    override fun toString(): String {
      return value.toString() + "L"
    }
  }

  class NbtFloat(float: Float): NbtTreeNode<Float>(){
    init { value = float; }

    override fun toString(): String {
      return value.toString() + "f"
    }
  }

  class NbtDouble(double: Double): NbtTreeNode<Double>(){
    init { value = double; }

    override fun toString(): String {
      return value.toString() + "d"
    }
  }

  class NbtBoolean(bool: Boolean): NbtTreeNode<Boolean>(){
    init { value = bool; }
  }

  class NbtCompound: NbtTreeNode<MutableMap<String, NbtTreeNode<Any>>>(), Iterable<Map.Entry<String, NbtTreeNode<Any>>>{
    init { value = HashMap(); }

     fun set(key: String, value: NbtTreeNode<Any>) { value()[key] = value }
     fun get(key: String): NbtTreeNode<Any> {
      return value()[key] ?: throw IllegalArgumentException("no such attribute named $key")
    }
     fun remove(key: String): NbtTreeNode<Any> {
      return value().remove(key)?: throw IllegalArgumentException("no such attribute named $key")
    }

    override fun iterator(): Iterator<Map.Entry<String, NbtTreeNode<Any>>> {
      return value().iterator()
    }

    override fun toString(): String {
      val builder = StringBuilder("{")

      value().forEach { entry ->
        builder.append("\"${entry.key}\":${entry.value},")
      }

      return builder.substring(0, builder.length - 1) + "}"
    }
  }

  open class NbtArray<T, Type: NbtTreeNode<T>>: NbtTreeNode<MutableList<Type>>(), Iterable<NbtTreeNode<T>>{
    init { value = ArrayList(); }

     fun set(index: Int, value: Type){ value()[index] = value; }
     fun get(index: Int): Type { return value()[index]; }
     fun append(value: Type){ value().add(value) }
     fun remove(index: Int): Type { return value().removeAt(index) }

    override fun iterator(): Iterator<NbtTreeNode<T>> {
      return value().iterator()
    }

    override fun toString(): String {
      val builder = StringBuilder("[")

      value().forEach {
        builder.append("$it,")
      }

      return builder.substring(0, builder.length - 1) + "]"
    }
  }
  class NbtByteArray: NbtArray<Byte, NbtByte>()
  class NbtIntArray: NbtArray<Int, NbtInt>()
  class NbtLongArray: NbtArray<Long, NbtLong>()
  class NbtList: NbtArray<Any, NbtTreeNode<Any>>()
}
