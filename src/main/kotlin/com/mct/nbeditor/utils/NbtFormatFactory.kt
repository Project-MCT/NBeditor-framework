package com.mct.nbeditor.utils

import sun.jvm.hotspot.code.Stub

class NbtFormatFactory{
  /**缩进长度*/
  var tabLen: Int = 0

  /**对数组类型显示元素的每行显示数目*/
  var arrayColumns: Int = 0
  /**若数组显示列数大于1,是否对齐（靠左）每一列元素*/
  var arrayColAlign: Boolean = false


  fun format(srcJSON: String): String{
    TODO("Not yet implemented")
  }
  fun rawJson(formattedJSON: String): String{
    TODO("Not yet implemented")
  }
}