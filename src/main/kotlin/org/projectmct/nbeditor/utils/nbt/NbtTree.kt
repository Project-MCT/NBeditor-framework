package org.projectmct.nbeditor.utils.nbt

class NbtTree: Cloneable{
  private var root: NbtCompound? = null

  constructor()

  constructor(src: NbtCompound){
    root = src
  }

  fun parseSNbt(json: String) {
    TODO("Not yet implemented")
  }

  fun set(root: NbtCompound){ this.root = root }
  fun toSNbt() = root.toString()
  fun root() = root?: throw IllegalStateException("this tree was not initialized")
  override fun clone() = NbtTree(root().clone())

  abstract class NbtTreeNode<Val>: Cloneable{
    protected var value: Val? = null

    fun value(): Val = value?: throw IllegalStateException("null value!")
    override fun toString() = value.toString()

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

    public abstract override fun clone(): NbtTreeNode<Val>
  }

  class NbtLabel(string: String) : NbtTreeNode<String>(){
    init { value = string; }

    override fun toString() = "\"${value()}\""
    override fun clone() = NbtLabel(value())
  }

  class NbtByte(byte: Byte): NbtTreeNode<Byte>(){
    init { value = byte; }

    override fun toString() = value.toString() + "b"
    override fun clone() = NbtByte(value())
  }

  class NbtShort(short: Short): NbtTreeNode<Short>(){
    init { value = short; }

    override fun toString() = value.toString() + "s"
    override fun clone() = NbtShort(value())
  }

  class NbtInt(int: Int): NbtTreeNode<Int>(){
    init { value = int; }

    override fun clone() = NbtInt(value())
  }

  class NbtLong(long: Long): NbtTreeNode<Long>(){
    init { value = long; }

    override fun toString() = value.toString() + "L"
    override fun clone() = NbtLong(value())
  }

  class NbtFloat(float: Float): NbtTreeNode<Float>(){
    init { value = float; }

    override fun toString() = value.toString() + "f"
    override fun clone() = NbtFloat(value())
  }

  class NbtDouble(double: Double): NbtTreeNode<Double>(){
    init { value = double; }

    override fun toString() = value.toString() + "d"
    override fun clone() = NbtDouble(value())
  }

  class NbtBoolean(bool: Boolean): NbtTreeNode<Boolean>(){
    init { value = bool; }

    override fun clone() = NbtBoolean(value())
  }

  class NbtCompound: NbtTreeNode<MutableMap<String, NbtTreeNode<*>>>(), Iterable<Map.Entry<String, NbtTreeNode<*>>>{
    init { value = LinkedHashMap(); }

    operator fun <T> set(key: String, value: NbtTreeNode<T>) { value()[key] = value }
    fun get(key: String) = value()[key] ?: throw IllegalArgumentException("no such attribute named $key")
    fun remove(key: String) = value().remove(key)?: throw IllegalArgumentException("no such attribute named $key")

    override fun iterator() = value().iterator()

    override fun toString(): String {
      val builder = StringBuilder("{")

      value().forEach { entry ->
        builder.append("\"${entry.key}\":${entry.value},")
      }

      return builder.substring(0, builder.length - 1) + "}"
    }

    override fun clone(): NbtCompound {
      val res = NbtCompound()

      for (entry in this) {
        res[entry.key] = entry.value.clone()
      }

      return res
    }
  }

  abstract class NbtArray<Type: NbtTreeNode<*>>: NbtTreeNode<MutableList<Type>>(), Iterable<Type>{
    init { value = ArrayList(); }

    fun set(index: Int, value: Type){ value()[index] = value; }
    fun get(index: Int): Type = value()[index]
    fun append(value: Type){ value().add(value) }
    fun remove(index: Int) : Type = value().removeAt(index)

    override fun iterator(): Iterator<Type> = value().iterator()

    override fun toString(): String {
      val builder = StringBuilder("[")

      value().forEach {
        builder.append("$it,")
      }

      return builder.substring(0, builder.length - 1) + "]"
    }

    @Suppress("UNCHECKED_CAST")
    protected fun copyTo(target: NbtArray<Type>): NbtArray<Type>{
      for (node in this) {
        target.append(node.clone() as Type)
      }

      return target
    }
  }

  class NbtByteArray: NbtArray<NbtByte>() {
    override fun clone() = copyTo(NbtByteArray()) as NbtByteArray
  }
  class NbtIntArray: NbtArray<NbtInt>() {
    override fun clone() = copyTo(NbtIntArray()) as NbtIntArray
  }
  class NbtLongArray: NbtArray<NbtLong>() {
    override fun clone() = copyTo(NbtLongArray()) as NbtLongArray
  }
  class NbtList: NbtArray<NbtTreeNode<*>>() {
    override fun clone() = copyTo(NbtList()) as NbtList
  }
}
