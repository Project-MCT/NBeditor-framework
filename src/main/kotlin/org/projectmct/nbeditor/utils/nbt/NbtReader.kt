package org.projectmct.nbeditor.utils.nbt

import org.projectmct.nbeditor.utils.nbt.NbtTree.Companion.ENDTAG
import org.projectmct.nbeditor.utils.nbt.nodes.*
import java.io.*
import java.nio.ByteBuffer

open class NbtReader(val input: DataInputStream): NbtDeserializer {
  constructor(inputStream: InputStream): this(DataInputStream(inputStream))
  constructor(file: File): this(DataInputStream(FileInputStream(file)))
  constructor(path: String): this(DataInputStream(FileInputStream(path)))
  constructor(bytes: ByteArray): this(DataInputStream(ByteArrayInputStream(bytes)))
  constructor(buffer: ByteBuffer): this(buffer.array())

  override fun readNbt(): NbtTree {
    val id = input.readByte().toInt()
    val name = input.readUTF()

    return NbtTree(name, readNext(id))
  }

  override fun readRaw(): NbtTreeNode<*> {
    val id = input.readByte().toInt()
    input.readUTF()

    return readNext(id)
  }

  protected open fun readNext(typeID: Int): NbtTreeNode<*> {
    return when(typeID){
      0 -> ENDTAG
      1 -> readByte()
      2 -> readShort()
      3 -> readInt()
      4 -> readLong()
      5 -> readFloat()
      6 -> readDouble()
      7 -> readByteArray()
      8 -> readLabel()
      9 -> readList()
      10 -> readCompound()
      11 -> readIntArray()
      12 -> readLongArray()
      else -> throw IllegalArgumentException("unknown tag typeID with \"$typeID\"")
    }
  }

  protected open fun readByte() = NbtByte(input.readByte())
  protected open fun readShort() = NbtShort(input.readShort())
  protected open fun readInt() = NbtInt(input.readInt())
  protected open fun readLong() = NbtLong(input.readLong())
  protected open fun readFloat() = NbtFloat(input.readFloat())
  protected open fun readDouble() = NbtDouble(input.readDouble())
  protected open fun readLabel() = NbtLabel(input.readUTF())

  protected open fun readByteArray(): NbtByteArray {
    val arr = NbtByteArray()
    val len = input.readInt()

    for (i in 0 until len) {
      arr.append(input.readByte())
    }
    return arr
  }
  protected open fun readIntArray(): NbtIntArray {
    val arr = NbtIntArray()
    val len = input.readInt()

    for (i in 0 until len) {
      arr.append(input.readInt())
    }
    return arr
  }
  protected open fun readLongArray(): NbtLongArray {
    val arr = NbtLongArray()
    val len = input.readInt()

    for (i in 0 until len) {
      arr.append(input.readLong())
    }
    return arr
  }

  protected open fun readList(): NbtList<*> {
    val listType: Byte = input.readByte()
    val list = NbtList<NbtTreeNode<*>>(NbtTree.getTypeClass(listType.toInt()))
    val length = input.readInt().minus(0)

    for (i in 0 until length) {
      list.append(readNext(listType.toInt()))
    }
    return list
  }
  protected open fun readCompound(): NbtCompound {
    val res = NbtCompound()
    var id = input.readByte().toInt().and(0xFF)

    while (id != 0) {
      val key = input.readUTF()
      val element = readNext(id)

      res[key] = element
      id = input.readByte().toInt().and(0xFF)
    }

    return res
  }
}