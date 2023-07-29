package org.projectmct.nbeditor.world;

import org.projectmct.nbeditor.utils.nbt.NbtTree

class Entity {
  val namespace: String
  val id: String
  val data: NbtTree

  constructor(namespace: String, id: String, data: NbtTree) {
    this.namespace = namespace
    this.id = id
    this.data = data
  }

  constructor(name: String, data: NbtTree) {
    val splits = name.split(":")

    namespace = splits[0]
    id = splits[1]
    this.data = data
  }
}
