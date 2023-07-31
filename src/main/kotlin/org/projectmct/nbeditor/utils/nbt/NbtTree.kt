package org.projectmct.nbeditor.utils.nbt

import org.projectmct.nbeditor.utils.nbt.nodes.*
import kotlin.reflect.KClass

class NbtTree: Cloneable{
  companion object{
    private val idMap: ArrayList<KClass<*>> = ArrayList()
    private val typeToIDMap: HashMap<KClass<*>, Byte> = HashMap()
    init {
      register(NbtEndMark::class)
      register(NbtByte::class)
      register(NbtShort::class)
      register(NbtInt::class)
      register(NbtLong::class)
      register(NbtFloat::class)
      register(NbtDouble::class)
      register(NbtByteArray::class)
      register(NbtLabel::class)
      register(NbtList::class)
      register(NbtCompound::class)
      register(NbtIntArray::class)
      register(NbtLongArray::class)
    }

    private fun register(type: KClass<*>){
      typeToIDMap[type] = idMap.size.toByte()
      idMap.add(type)
    }

    fun getTypeClass(id: Int) = if (id < 12){ idMap[id] }
        else throw IllegalArgumentException("unknown type id with: \"$id\"")
    fun getTypeID(clazz: KClass<*>) = typeToIDMap[clazz]?: throw IllegalArgumentException("unknown type class with: \"$clazz\"")

    val ENDTAG = NbtEndMark.INSTANCE
  }

  var name: String = ""
  private var root: NbtTreeNode<*>? = null

  constructor()

  constructor(name: String, src: NbtTreeNode<*>){
    this.name = name
    root = src
  }
  constructor(tree: NbtTree){
    this.root = tree.root
    this.name = tree.name
  }

  fun set(name: String, root: NbtTreeNode<*>){
    this.root = root
    this.name = name
  }
  fun set(tree: NbtTree){
    this.root = tree.root
    this.name = tree.name
  }
  fun toSNbt() = "\"$name\": ${root.toString()}"
  fun root() = root?: throw IllegalStateException("this tree was not initialized")
  override fun clone() = NbtTree(name?:"", root().clone())
}
