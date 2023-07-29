package org.projectmct.nbeditor.utils;

class Point(val x: Int = 0, val y: Int = 0, val z: Int = 0) {
  private val hash = x*y.rotateLeft(9)*z.rotateLeft(18)

  override fun hashCode() = hash
  override fun equals(other: Any?) = other is Point && other.x == x && other.y == y && other.z == z
}
