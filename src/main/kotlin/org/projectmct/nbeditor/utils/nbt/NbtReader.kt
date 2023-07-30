package org.projectmct.nbeditor.utils.nbt

import org.projectmct.nbeditor.utils.nbt.NbtTree.*
import org.projectmct.nbeditor.utils.nbt.NbtTree.Companion.ENDTAG
import java.io.*
import java.nio.ByteBuffer

open class NbtReader(val input: DataInputStream) {
  constructor(inputStream: InputStream): this(DataInputStream(inputStream))
  constructor(file: File): this(DataInputStream(FileInputStream(file)))
  constructor(path: String): this(DataInputStream(FileInputStream(path)))
  constructor(bytes: ByteArray): this(DataInputStream(ByteArrayInputStream(bytes)))
  constructor(buffer: ByteBuffer): this(buffer.array())

  fun readRawNode(): NbtCompound{
    val res = NbtCompound()

    val id = input.readByte().toInt()
    val name = input.readUTF()
    res[name] = readNext(id)

    return res
  }
  fun readNbt(): NbtTree = NbtTree(readRawNode())
  fun readToTree(tree: NbtTree){ tree.set(readCompound()) }

  protected fun readNext(typeID: Int): NbtTreeNode<*>{
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

  protected fun readByte() = NbtByte(input.readByte())
  protected fun readShort() = NbtShort(input.readShort())
  protected fun readInt() = NbtInt(input.readInt())
  protected fun readLong() = NbtLong(input.readLong())
  protected fun readFloat() = NbtFloat(input.readFloat())
  protected fun readDouble() = NbtDouble(input.readDouble())
  protected fun readLabel() = NbtLabel(input.readUTF())

  protected fun readByteArray(): NbtByteArray {
    val arr = NbtByteArray()
    val len = input.readInt()

    for (i in 0 until len) {
      arr.append(input.readByte())
    }
    return arr
  }
  protected fun readIntArray(): NbtIntArray {
    val arr = NbtIntArray()
    val len = input.readInt()

    for (i in 0 until len) {
      arr.append(input.readInt())
    }
    return arr
  }
  protected fun readLongArray(): NbtLongArray {
    val arr = NbtLongArray()
    val len = input.readInt()

    for (i in 0 until len) {
      arr.append(input.readLong())
    }
    return arr
  }

  protected fun readList(): NbtList<*> {
    val listType: Byte = input.readByte()
    val list = NbtList<NbtTreeNode<*>>(NbtTree.getTypeClass(listType.toInt()))
    val length = input.readInt().minus(0)

    for (i in 0 until length) {
      list.append(readNext(listType.toInt()))
    }
    return list
  }
  protected fun readCompound(): NbtCompound {
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