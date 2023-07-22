package implutils

import com.mct.nbeditor.nbtutils.NbtTree
import com.mct.nbeditor.nbtutils.NbtTreeNode

class ImplNbtTree: NbtTree {
  override var nodes: Int = 0
  override var root: NbtTreeNode.NbtCompound? = null

  override fun parseSNbt(json: String) {

  }

  override fun toSNbt(): String {
    return root().toString()
  }

  open class ImplNbtTreeNode<Val> : NbtTreeNode<Val> {
    protected var value: Val? = null

    override fun getValue(): Val {
      return value?: throw IllegalStateException("null value!")
    }

    override fun toString(): String{
      return value.toString()
    }

    override fun asLabel(): NbtTreeNode.NbtLabel {
      if (this !is NbtTreeNode.NbtLabel) throw ClassCastException("node is ${this::class}, can not be cast to Label"); return this
    }

    override fun asByte(): NbtTreeNode.NbtByte {
      if (this !is NbtTreeNode.NbtByte) throw ClassCastException("node is ${this::class}, can not be cast to Byte"); return this
    }

    override fun asShort(): NbtTreeNode.NbtShort {
      if (this !is NbtTreeNode.NbtShort) throw ClassCastException("node is ${this::class}, can not be cast to Short"); return this
    }

    override fun asInt(): NbtTreeNode.NbtInt {
      if (this !is NbtTreeNode.NbtInt) throw ClassCastException("node is ${this::class}, can not be cast to Integer"); return this
    }

    override fun asLong(): NbtTreeNode.NbtLong {
      if (this !is NbtTreeNode.NbtLong) throw ClassCastException("node is ${this::class}, can not be cast to Long"); return this
    }

    override fun asFloat(): NbtTreeNode.NbtFloat {
      if (this !is NbtTreeNode.NbtFloat) throw ClassCastException("node is ${this::class}, can not be cast to Float"); return this
    }

    override fun asDouble(): NbtTreeNode.NbtDouble {
      if (this !is NbtTreeNode.NbtDouble) throw ClassCastException("node is ${this::class}, can not be cast to Double"); return this
    }

    override fun asArray(): NbtTreeNode.NbtList {
      if (this !is NbtTreeNode.NbtList) throw ClassCastException("node is ${this::class}, can not be cast to Array"); return this
    }
    override fun asObject(): NbtTreeNode.NbtCompound {
      if (this !is NbtTreeNode.NbtCompound) throw ClassCastException("node is ${this::class}, can not be cast to js object"); return this
    }
  }

  class ImplNbtLabel(string: String) : ImplNbtTreeNode<String>(), NbtTreeNode.NbtLabel {
    init { value = string; }

    override fun toString(): String {
      return "\"${getValue()}\""
    }
  }

  class ImplNbtByte(byte: Byte): ImplNbtTreeNode<Byte>(), NbtTreeNode.NbtByte{
    init { value = byte; }

    override fun toString(): String {
      return value.toString() + "b"
    }
  }

  class ImplNbtShort(short: Short): ImplNbtTreeNode<Short>(), NbtTreeNode.NbtShort{
    init { value = short; }

    override fun toString(): String {
      return value.toString() + "s"
    }
  }

  class ImplNbtInt(int: Int): ImplNbtTreeNode<Int>(), NbtTreeNode.NbtInt{
    init { value = int; }
  }

  class ImplNbtLong(long: Long): ImplNbtTreeNode<Long>(), NbtTreeNode.NbtLong{
    init { value = long; }

    override fun toString(): String {
      return value.toString() + "L"
    }
  }

  class ImplNbtFloat(float: Float): ImplNbtTreeNode<Float>(), NbtTreeNode.NbtFloat{
    init { value = float; }

    override fun toString(): String {
      return value.toString() + "f"
    }
  }

  class ImplNbtDouble(double: Double): ImplNbtTreeNode<Double>(), NbtTreeNode.NbtDouble{
    init { value = double; }

    override fun toString(): String {
      return value.toString() + "d"
    }
  }

  class ImplNbtBoolean(bool: Boolean): ImplNbtTreeNode<Boolean>(), NbtTreeNode.NbtBoolean{
    init { value = bool; }
  }

  class ImplNbtCompound: ImplNbtTreeNode<MutableMap<String, NbtTreeNode<Any>>>(),
    NbtTreeNode.NbtCompound {
    init { value = HashMap(); }

    override fun set(key: String, value: NbtTreeNode<Any>) { getValue()[key] = value }
    override fun get(key: String): NbtTreeNode<Any> {
      return getValue()[key] ?: throw IllegalArgumentException("no such attribute named $key")
    }
    override fun remove(key: String): NbtTreeNode<Any> {
      return getValue().remove(key)?: throw IllegalArgumentException("no such attribute named $key")
    }

    override fun toString(): String {
      val builder = StringBuilder("{")

      getValue().forEach {entry ->
        builder.append("\"${entry.key}\":${entry.value},")
      }

      return builder.substring(0, builder.length - 1) + "}"
    }
  }

  open class ImplNbtArray<T, Type: NbtTreeNode<T>>: ImplNbtTreeNode<MutableList<Type>>(), NbtTreeNode.NbtArray<T, Type> {
    init { value = ArrayList(); }

    override fun set(index: Int, value: Type){ getValue()[index] = value; }
    override fun get(index: Int): Type { return getValue()[index]; }
    override fun append(value: Type){ getValue().add(value) }
    override fun remove(index: Int): Type { return getValue().removeAt(index) }

    override fun toString(): String {
      val builder = StringBuilder("[")

      getValue().forEach {
        builder.append("$it,")
      }

      return builder.substring(0, builder.length - 1) + "]"
    }
  }
  class ImplNbtByteArray: ImplNbtArray<Byte, NbtTreeNode.NbtByte>(), NbtTreeNode.NbtByteArray
  class ImplNbtIntArray: ImplNbtArray<Int, NbtTreeNode.NbtInt>(), NbtTreeNode.NbtIntArray
  class ImplNbtLongArray: ImplNbtArray<Long, NbtTreeNode.NbtLong>(), NbtTreeNode.NbtLongArray
  class ImplNbtList: ImplNbtArray<Any, NbtTreeNode<Any>>(), NbtTreeNode.NbtList
}
