package org.projectmct.nbeditor.utils;

class Point2D(val x: Int, val y: Int) {
  private val hash = x*y.rotateLeft(16)

  override fun hashCode() = hash
  override fun equals(other: Any?) = other is Point2D && other.x == x && other.y == y
}
