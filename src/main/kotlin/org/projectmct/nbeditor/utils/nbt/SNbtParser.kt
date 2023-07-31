package org.projectmct.nbeditor.utils.nbt;

import org.projectmct.nbeditor.utils.nbt.nodes.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringReader
import java.lang.RuntimeException
import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue
import kotlin.reflect.KClass

open class SNbtParser(private val reader: Reader): NbtDeserializer {
  constructor(snbt: String): this(StringReader(snbt))
  constructor(file: File): this(BufferedReader(FileReader(file)))
  constructor(inputStream: InputStream): this(BufferedReader(InputStreamReader(inputStream)))

  var detailInfoSize = 18
  var detailAfterSize = 6

  var line = 1
  var column = 0

  init {
    if (!reader.markSupported())
      throw IllegalArgumentException("require mark supported reader")
  }

  private val readCached = ArrayList<Char>()
  private fun cacheChar(char: Char){
    readCached.add(if (char.isWhitespace()) ' ' else char)
    if (readCached.size > detailInfoSize) readCached.removeFirst()

    if (char == '\n'){
      column = 0
      line++
    }
    column++
  }

  override fun readNbt(): NbtTree {
    readCached.clear()
    line = 0
    column = 0

    var name: String? = null
    while (true) {
      var ch = reader.read()
      cacheChar(ch.toChar())
      while (ch != -1 && ch.toChar().isWhitespace()){
        ch = reader.read()
        cacheChar(ch.toChar())
      }

      if (ch == -1) throw LexerCheckException("input is empty!", false)

      val token = ch.toChar()
      if (token == ':'){
        if (name == null) throw LexerCheckException("unexpect symbol with \":\"", true)
        continue
      }

      if (name == null) {
        name = readNode<NbtLabel>(token).value()
      }
      else return NbtTree(name, readNode(ch.toChar()))
    }
  }
  override fun readRaw() = readNbt().root()

  @Suppress("UNCHECKED_CAST")
  protected open fun <Ret: NbtTreeNode<*>>readNode(c: Char): Ret {
    return when(c){
      '"' -> readLable() as Ret
      '[' -> readList()
      '{' -> readCompound() as Ret
      else -> if (c.isDigit() || c == '-'){
        reader.reset()
        readCached.removeLast()
        readNumber<Ret>()
      }
      else throw LexerCheckException("unexpect symbol \"$c\"", true)
    }
  }

  @Suppress("UNCHECKED_CAST")
  protected open fun <Ret: NbtTreeNode<*>>readNumber(): Ret{
    val stringBuffer = StringBuffer()

    var head = true
    var decimal = false
    while (true) {
      reader.mark(1)
      val c = reader.read()
      cacheChar(c.toChar())
      if (c == -1) throw LexerCheckException("unknown error", true)
      val token = c.toChar()

      if (token == '-') { if (head) stringBuffer.append(token) else throw LexerCheckException("unexpect symbol with \"-\"", true) }
      else if (token.isDigit()) stringBuffer.append(token)
      else if (token == '.'){
        if (decimal)
          throw LexerCheckException("unexpect symbol \".\"", true)

        stringBuffer.append(token)
        decimal = true
      }
      else when(token.lowercase()[0]){
        'b' -> return NbtByte(stringBuffer.toString().toByte()) as Ret
        's' -> return NbtShort(stringBuffer.toString().toShort()) as Ret
        'l' -> return NbtLong(stringBuffer.toString().toLong()) as Ret
        'd' -> return NbtDouble(stringBuffer.toString().toDouble()) as Ret
        'f' -> return NbtFloat(stringBuffer.toString().toFloat()) as Ret
        else -> {
          reader.reset()
          readCached.removeLast()

          if (decimal) throw LexerCheckException("a decimal must declare F(float) or D(double) at end, but no", true)
          return NbtInt(stringBuffer.toString().toInt()) as Ret
        }
      }

      head = false
    }
  }

  protected open fun readLable(): NbtLabel {
    val stringBuilder = StringBuilder()

    var escape = false
    while (true){
      val c = reader.read()
      cacheChar(c.toChar())
      if (c == -1) throw LexerCheckException("\"{\" does not match the quantity of \"}\"", false)
      val token = c.toChar()

      if (!escape) {
        if (token == '\\') {
          escape = true
          continue
        }
        else if (token == '"') return NbtLabel(stringBuilder.toString())
      }
      else escape = false

      stringBuilder.append(token)
    }
  }

  @Suppress("UNCHECKED_CAST")
  protected open fun <Ret> readList(): Ret {
    val tmp = ArrayList<NbtTreeNode<*>>()
    var numberType: KClass<*>? = null

    reader.mark(1)
    val c = reader.read()
    cacheChar(c.toChar())
    if (c == -1)
      throw LexerCheckException("\"[\" does not match the quantity of \"]\"", false)

    when(c.toChar()){
      'B' -> numberType = NbtByteArray::class
      'I' -> numberType = NbtIntArray::class
      'L' -> numberType = NbtLongArray::class
    }

    val next = if (numberType != null){ val r = reader.read().toChar(); cacheChar(r); r } else null
    if (next != null && next != ';')
      throw LexerCheckException("expect \";\", but found \"${next.toChar()}\"", true)

    while (true){
      reader.mark(1)
      val ch = reader.read()
      cacheChar(ch.toChar())
      if (ch == -1) throw LexerCheckException("\"[\" does not match the quantity of \"]\"", false)

      val token = ch.toChar()
      if (token.isWhitespace() || token == ',') continue
      else if (token == ']') break

      val node = readNode<NbtTreeNode<*>>(token)
      if (tmp.isNotEmpty() && tmp.last()::class != node::class)
        throw LexerCheckException("array element must be same type, but existed two and more", true)

      tmp.add(node)
    }

    if (numberType != null){
      when(numberType){
        NbtByteArray::class -> {
          val res = NbtByteArray()
          for (v in tmp) { res.append(v.asByte().value()) }
          return res as Ret
        }
        NbtIntArray::class -> {
          val res = NbtIntArray()
          for (v in tmp) { res.append(v.asInt().value()) }
          return res as Ret
        }
        NbtLongArray::class -> {
          val res = NbtLongArray()
          for (v in tmp) { res.append(v.asLong().value()) }
          return res as Ret
        }
        else -> throw RuntimeException("? what?")
      }
    }
    else {
      val head = if (tmp.isEmpty()) null else tmp.first()
      return if (head == null) NbtList.makeList<NbtLabel>() as Ret
        else {
          val res: NbtList<NbtTreeNode<*>>
          try {
            res = NbtList(head::class)
          }
          catch (e: IllegalArgumentException){
            throw LexerCheckException(e.message?:"", true)
          }
          for (v in tmp) {
            res.append(v)
          }
          res as Ret
        }
    }
  }

  protected open fun readCompound(): NbtCompound {
    val res = NbtCompound()

    var key: NbtLabel? = null
    var getValue = false
    while (true){
      reader.mark(1)
      val c = reader.read()
      cacheChar(c.toChar())
      if (c == -1) throw LexerCheckException("\"{\" does not match the quantity of \"}\"", false)

      val token = c.toChar()
      if (token.isWhitespace()) continue
      else if (token == ',') {
        if(key != null) throw LexerCheckException("unexpect symbol \",\"", true)
        continue
      }
      else if (token == ':'){
        getValue = true
        continue
      }
      else if (token == '}') break

      if (getValue){
        if (key == null) throw LexerCheckException("unexpect symbol \":\"", true)
        res[key.value()] = readNode<NbtTreeNode<*>>(token)
        key = null
        getValue = false
        continue
      }
      else {
        if (key == null){
          if (token != '"') throw LexerCheckException("expect '\"', but find \"$token\"", true)
          key = readNode(token)
        }
        else throw LexerCheckException("expect \":\", but find \"$token\"", true)
      }
    }

    return res
  }

  private fun genMessage(msg: String): String{
    val detail = StringBuilder("    ")
    readCached.forEach(detail::append)
    for (i in 1..detailAfterSize) {
      val c = reader.read()
      if (c == -1) break
      detail.append(if (c.toChar().isWhitespace()) ' ' else c.toChar())
    }
    detail.append("\n    ")
    for (i in 1..<readCached.size) {
      detail.append(' ')
    }
    detail.append('^')

    return "$msg, where($line, $column):\n\n$detail"
  }
  inner class LexerCheckException(msg: String, details: Boolean) : RuntimeException(if (details) genMessage(msg) else msg)
}

