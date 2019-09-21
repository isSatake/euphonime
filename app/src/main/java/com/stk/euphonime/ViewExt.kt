package com.stk.euphonime

import android.graphics.Rect
import android.view.View

/**
 * 指定座標がView範囲内かどうか
 * https://qiita.com/_meki/items/2bfc595431d28dfe1143 より借用
 */
fun View.inBounds(x: Int, y: Int): Boolean {
  val outRect = Rect()
  getDrawingRect(outRect)
  val location = IntArray(2)
  getLocationOnScreen(location)
  outRect.offset(location[0], location[1])
  return outRect.contains(x, y)
}