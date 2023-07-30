package org.projectmct.nbeditor.utils.nbt

import java.awt.Label
import kotlin.reflect.KClass

class NbtTree: Cloneable{
  companion object{
    private val idMap: ArrayList<KClass<*>> = ArrayList()
    init {
      idMap.add(NbtEndMark::class)
      idMap.add(NbtByte::class)
      idMap.add(NbtShort::class)
      idMap.add(NbtInt::class)
      idMap.add(NbtLong::class)
      idMap.add(NbtFloat::class)
      idMap.add(NbtDouble::class)
      idMap.add(NbtByteArray::class)
      idMap.add(NbtLabel::class)
      idMap.add(NbtList::class)
      idMap.add(NbtCompound::class)
      idMap.add(NbtIntArray::class)
      idMap.add(NbtLongArray::class)
    }

    fun getTypeClass(id: Int) = if (id < 12){ idMap[id] }
        else throw IllegalArgumentException("unknown type id with: \"$id\"")

    val ENDTAG = NbtTree.NbtEndMark.INSTANCE
  }

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

    abstract fun typeID(): Int

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

    fun asByteArray(): NbtByteArray{
      if (this !is NbtByteArray) throw ClassCastException("node is ${this::class}, can not be cast to byte array"); return this
    }
    fun asIntArray(): NbtIntArray{
      if (this !is NbtIntArray) throw ClassCastException("node is ${this::class}, can not be cast to int array"); return this
    }
    fun asLongArray(): NbtLongArray{
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

  class NbtEndMark private constructor() : NbtTreeNode<Unit>() {
    companion object{
      val INSTANCE = NbtEndMark()
    }

    override fun toString() = ""
    override fun typeID() = 0

    override fun clone() = INSTANCE
  }

  class NbtLabel(string: String) : NbtTreeNode<String>(){
    init { value = string; }

    override fun toString() = "\"${value()}\""
    override fun typeID() = 8
    override fun clone() = NbtLabel(value())
  }

  class NbtByte(byte: Byte): NbtTreeNode<Byte>(){
    init { value = byte; }

    override fun toString() = value.toString() + "b"
    override fun typeID() = 1
    override fun clone() = NbtByte(value())
  }

  class NbtShort(short: Short): NbtTreeNode<Short>(){
    init { value = short; }

    override fun toString() = value.toString() + "s"
    override fun typeID() = 2
    override fun clone() = NbtShort(value())
  }

  class NbtInt(int: Int): NbtTreeNode<Int>(){
    init { value = int; }

    override fun typeID() = 3
    override fun clone() = NbtInt(value())
  }

  class NbtLong(long: Long): NbtTreeNode<Long>(){
    init { value = long; }

    override fun toString() = value.toString() + "L"
    override fun typeID() = 4
    override fun clone() = NbtLong(value())
  }

  class NbtFloat(float: Float): NbtTreeNode<Float>(){
    init { value = float; }

    override fun toString() = value.toString() + "f"
    override fun typeID() = 5
    override fun clone() = NbtFloat(value())
  }

  class NbtDouble(double: Double): NbtTreeNode<Double>(){
    init { value = double; }

    override fun toString() = value.toString() + "d"
    override fun typeID() = 6
    override fun clone() = NbtDouble(value())
  }

  class NbtCompound: NbtTreeNode<MutableMap<String, NbtTreeNode<*>>>(), Iterable<Map.Entry<String, NbtTreeNode<*>>>{
    init { value = LinkedHashMap(); }

    operator fun <T> set(key: String, value: NbtTreeNode<T>) { value()[key] = value }
    fun get(key: String) = value()[key] ?: throw IllegalArgumentException("no such attribute named $key")
    fun remove(key: String) = value().remove(key)?: throw IllegalArgumentException("no such attribute named $key")

    override fun iterator() = value().iterator()
    override fun typeID() = 10

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

  abstract class NbtArray<Type: Any>: NbtTreeNode<MutableList<Type>>(), Iterable<Type>{
    init { value = ArrayList(); }

    abstract var elemType: KClass<*>

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
        target.append(if (node is NbtTreeNode<*>) {node.clone() as Type} else node)
      }

      return target
    }

    private fun checkType(target: Any){
      if (target == NbtEndMark::class)
        throw IllegalArgumentException("end tag was invalid in list!")

      if (elemType != target::class)
        throw IllegalArgumentException("array type \"$elemType\", but handling a value with type: \"${target::class}\"")
    }
  }

  class NbtByteArray: NbtArray<Byte>() {
    override var elemType: KClass<*>
      get() = Byte::class
      set(value) {}

    override fun typeID() = 7
    override fun clone() = copyTo(NbtByteArray()) as NbtByteArray
  }
  class NbtIntArray: NbtArray<Int>() {
    override var elemType: KClass<*>
      get() = Int::class
      set(value) {}

    override fun typeID() = 11
    override fun clone() = copyTo(NbtIntArray()) as NbtIntArray
  }
  class NbtLongArray: NbtArray<Long>() {
    override var elemType: KClass<*>
      get() = Long::class
      set(value) {}

    override fun typeID() = 12
    override fun clone() = copyTo(NbtLongArray()) as NbtLongArray
  }
  class NbtList<Ty: NbtTreeNode<*>>(override var elemType: KClass<*>) : NbtArray<Ty>() {
    override fun typeID() = 8
    override fun clone() = copyTo(NbtList(elemType)) as NbtList
  }
}
