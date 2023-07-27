package org.projectmct.nbeditor.nbtutils

import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.ByteBuffer

class NbtReader(val input: InputStream) {
  constructor(file: File): this(FileInputStream(file))
  constructor(path: String): this(FileInputStream(path))
  constructor(bytes: ByteArray): this(ByteArrayInputStream(bytes))
  constructor(buffer: ByteBuffer): this(buffer.array())

  fun readToTree(tree: NbtTree){
    TODO("Not yet implemented")
  }
}