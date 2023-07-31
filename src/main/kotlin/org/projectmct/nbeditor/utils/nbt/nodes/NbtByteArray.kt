package org.projectmct.nbeditor.utils.nbt.nodes

import kotlin.reflect.KClass

class NbtByteArray: NbtArray<Byte>() {
  override val elemType: KClass<*>
    get() = Byte::class

  companion object { const val ID: Byte = 7 }

  override fun typeID() = ID
  override fun toString(): String {
    val li = super.toString()
    return "[B;" + li.substring(1, li.length)
  }
  override fun clone() = copyTo(NbtByteArray()) as NbtByteArray
}