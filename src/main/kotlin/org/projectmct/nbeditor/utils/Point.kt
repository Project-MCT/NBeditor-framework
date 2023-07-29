package org.projectmct.nbeditor.utils;

class Point(val x: Int, val y: Int, val z: Int) {
  private val hash = x*y.rotateLeft(9)*z.rotateLeft(18)

  override fun hashCode() = hash
  override fun equals(other: Any?) = other is Point && other.x == x && other.y == y && other.z == z
}
