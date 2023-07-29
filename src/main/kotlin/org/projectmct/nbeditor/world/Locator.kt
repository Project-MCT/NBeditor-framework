package org.projectmct.nbeditor.world

import org.projectmct.nbeditor.utils.Point

abstract class Locator {
  protected var currLocating: World? = null
  protected var originPos: Point? = Point()

  abstract fun initialize(world: World, searchOrigin: Point)
  abstract fun findNext(): Point
  abstract fun list(beginPos: Point, endPos: Point): Array<Point>
  fun findLocates(counts: Int) = Array(counts) { findNext() }
}