package com.mct.nbeditor.utils

interface NbtFormatFactory {
  /**缩进长度*/
  var tabLen: Int

  /**对数组类型显示元素的每行显示数目*/
  var arrayColumns: Int
  /**若数组显示列数大于1,是否对齐（靠左）每一列元素*/
  var arrayColAlign: Boolean


  fun format(srcJSON: String): String
  fun rawJson(formattedJSON: String): String
}