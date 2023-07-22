package com.mct.nbeditor.utils

interface File {
  fun mkdirs(): Boolean
  fun delete(): Boolean

  fun path(): String
  fun child(fName: String): File
  fun fileList(): Array<File>

  fun exists(): Boolean
  fun isDirection(): Boolean

  fun openReader(): Reader
  fun openWriter(): Writer
}