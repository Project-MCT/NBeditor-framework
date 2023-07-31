package org.projectmct.nbeditor.utils.nbt.nodes

import kotlin.reflect.KClass

class NbtLongArray: NbtArray<Long>() {
  override val elemType: KClass<*>
    get() = Long::class

  companion object { const val ID: Byte = 12 }

  override fun typeID() = ID
  override fun toString(): String {
    val li = super.toString()
    return "[L;" + li.substring(1, li.length)
  }
  override fun clone() = copyTo(NbtLongArray()) as NbtLongArray
}