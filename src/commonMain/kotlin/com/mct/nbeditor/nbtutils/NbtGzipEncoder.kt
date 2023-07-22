package com.mct.nbeditor.nbtutils

interface NbtGzipEncoder {
  fun encode(sNbt: String): Array<Byte>
  fun decode(nbt: Array<Byte>): String
}