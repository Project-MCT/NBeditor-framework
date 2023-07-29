package org.projectmct.nbeditor.utils.nbt

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class NbtWriter(val output: OutputStream) {
  constructor(file: File): this(FileOutputStream(file))
  constructor(path: String): this(FileOutputStream(path))

  constructor(): this(ByteArrayOutputStream())

  fun writeToStream(tree: NbtTree){
    TODO("Not yet implemented")
  }
}