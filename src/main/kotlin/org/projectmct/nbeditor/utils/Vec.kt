package org.projectmct.nbeditor.utils

import kotlin.math.sqrt

class Vec(var x: Double, var y: Double, var z: Double) {
  fun len2() = x*x + y*y + z*z
  fun len() = sqrt(len2())

  fun add(x: Double, y: Double, z: Double): Vec{
    this.x += x
    this.y += y
    this.z += z
    return this
  }
  fun sub(x: Double, y: Double, z: Double): Vec{
    this.x -= x
    this.y -= y
    this.z -= z
    return this
  }
  fun mul(x: Double, y: Double, z: Double): Vec{
    this.x *= x
    this.y *= y
    this.z *= z
    return this
  }
  fun dvn(x: Double, y: Double, z: Double): Vec{
    this.x /= x
    this.y /= y
    this.z /= z
    return this
  }

  fun add(vec: Vec) = add(vec.x, vec.y, vec.z)
  fun sub(vec: Vec) = sub(vec.x, vec.y, vec.z)
  fun mul(vec: Vec) = mul(vec.x, vec.y, vec.z)
  fun dvn(vec: Vec) = dvn(vec.x, vec.y, vec.z)

  override fun hashCode() = (x*(y + 32)*(z + 256)).toInt()
  override fun equals(other: Any?) = other is Vec && other.x == x && other.y == y && other.z == z

}