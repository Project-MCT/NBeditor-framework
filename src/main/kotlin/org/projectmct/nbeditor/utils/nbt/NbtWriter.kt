package org.projectmct.nbeditor.utils.nbt

import org.projectmct.nbeditor.utils.nbt.nodes.*
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class NbtWriter(val output: DataOutputStream): NbtTreeVisitor<Unit>(), NbtSerializer {
  constructor(output: OutputStream): this(DataOutputStream(output))
  constructor(file: File): this(FileOutputStream(file))
  constructor(path: String): this(FileOutputStream(path))

  constructor(): this(ByteArrayOutputStream())

  override fun write(tree: NbtTree) {
    output.writeByte(tree.root().typeID().toInt())
    output.writeUTF(tree.name)
    visit(tree.root())
  }

  override fun writeRaw(node: NbtTreeNode<*>) {
    output.writeByte(node.typeID().toInt())
    output.writeUTF("")
    visit(node)
  }

  override fun visitCompound(compound: NbtCompound, arg: Unit?) {
    super.visitCompound(compound, arg)
    output.writeByte(0)
  }

  override fun visitAttribute(name: String, value: NbtTreeNode<*>, arg: Unit?) {
    output.writeByte(value.typeID().toInt())
    output.writeUTF(name)
    super.visitAttribute(name, value, arg)
  }

  override fun visitList(arr: NbtList<*>, arg: Unit?) {
    output.writeByte(NbtTree.getTypeID(arr.elemType).toInt())
    output.writeInt(arr.length)
    super.visitList(arr, arg)
  }

  override fun visitLabel(label: NbtLabel, arg: Unit?) {
    output.writeUTF(label.value())
  }
  override fun visitByte(byte: NbtByte, arg: Unit?) {
    output.writeByte(byte.value().toInt())
  }
  override fun visitShort(short: NbtShort, arg: Unit?) {
    output.writeShort(short.value().toInt())
  }
  override fun visitInt(int: NbtInt, arg: Unit?) {
    output.writeInt(int.value())
  }
  override fun visitLong(long: NbtLong, arg: Unit?) {
    output.writeLong(long.value())
  }
  override fun visitFloat(float: NbtFloat, arg: Unit?) {
    output.writeFloat(float.value())
  }
  override fun visitDouble(double: NbtDouble, arg: Unit?) {
    output.writeDouble(double.value())
  }

  override fun visitByteArray(byteArray: NbtByteArray, arg: Unit?) {
    output.writeInt(byteArray.length)
    for (byte in byteArray) output.writeByte(byte.toInt())
  }
  override fun visitIntArray(intArray: NbtIntArray, arg: Unit?) {
    output.writeInt(intArray.length)
    for (int in intArray) output.writeInt(int)
  }
  override fun visitLongArray(longArray: NbtLongArray, arg: Unit?) {
    output.writeInt(longArray.length)
    for (long in longArray) output.writeLong(long)
  }
}