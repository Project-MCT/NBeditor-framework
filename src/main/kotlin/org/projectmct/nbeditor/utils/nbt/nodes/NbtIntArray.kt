package org.projectmct.nbeditor.utils.nbt.nodes

import kotlin.reflect.KClass

class NbtIntArray: NbtArray<Int>() {
  override val elemType: KClass<*>
    get() = Int::class

  companion object { const val ID: Byte = 11 }

  override fun typeID() = ID
  override fun toString(): String {
    val li = super.toString()
    return "[I;" + li.substring(1, li.length)
  }
  override fun clone() = copyTo(NbtIntArray()) as NbtIntArray
}