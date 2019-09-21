package com.stk.euphonime

import android.inputmethodservice.InputMethodService
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.View.GONE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stk.euphonime.databinding.KeyboardBinding
import timber.log.Timber


class EuphonIME : InputMethodService() {
  private lateinit var binding: KeyboardBinding
  private lateinit var firstViewAdapter: RecyclerView.Adapter<*>
  private lateinit var firstViewManager: RecyclerView.LayoutManager
  private lateinit var secondViewAdapter: RecyclerView.Adapter<*>
  private lateinit var secondViewManager: RecyclerView.LayoutManager

  private val firstKeys = arrayOf("//", "/", "", "2", "4", "8")
  private val secondKeys = arrayOf(
    "b,,,,,", "b,,,,", "b,,,", "b,,", "b,", "b", "b'", "b''", "b'''",
    "a,,,,,", "a,,,,", "a,,,", "a,,", "a,", "a", "a'", "a''", "a'''",
    "g,,,,,", "g,,,,", "g,,,", "g,,", "g,", "g", "g'", "g''", "g'''",
    "f,,,,,", "f,,,,", "f,,,", "f,,", "f,", "f", "f'", "f''", "f'''",
    "e,,,,,", "e,,,,", "e,,,", "e,,", "e,", "e", "e'", "e''", "e'''",
    "d,,,,,", "d,,,,", "d,,,", "d,,", "d,", "d", "d'", "d''", "d'''",
    "c,,,,,", "c,,,,", "c,,,", "c,,", "c,", "c", "c'", "c''", "c'''"
  )

  private var firstText = ""

  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())

    firstViewManager = GridLayoutManager(this, firstKeys.size)
    secondViewManager = GridLayoutManager(this, 9)
    firstViewAdapter = FirstKeyAdapter(firstKeys, ::onTouchFirstKey)
    secondViewAdapter = SecondKeyAdapter(secondKeys, ::onTouchSecondKey)
    // TODO: currentInputConnectionがどのように更新されるのか知りたい
  }

  override fun onCreateInputView(): View {
    binding = DataBindingUtil.inflate(layoutInflater, R.layout.keyboard, null, false)
    binding.firstKeys.apply {
      setHasFixedSize(true)
      layoutManager = firstViewManager
      adapter = firstViewAdapter
      setOnTouchListener { _, event ->
        // secondKeysではACTION_UPのみ必要だが、それだけ送ってもイベントハンドラが反応しない
        binding.secondKeys.dispatchTouchEvent(event)

        // second_key_viewを見せる
        if (event.action == ACTION_DOWN) alpha = 0f

        false
      }
    }
    binding.secondKeys.apply {
      setHasFixedSize(true)
      layoutManager = secondViewManager
      adapter = secondViewAdapter
      isNestedScrollingEnabled = false
      setOnTouchListener { _, event ->
        when (event.action) {
          ACTION_MOVE -> {
            // プレビュー
            onHoverSecondKey(event, getSecondKeyFromTouchEvent(event))
            return@setOnTouchListener true
          }
          ACTION_UP -> {
            // 指を離した座標からSecondKeyを計算
            onTouchSecondKey(getSecondKeyFromTouchEvent(event))
            return@setOnTouchListener true
          }
        }
        true
      }
    }
    return binding.root
  }

  private fun onTouchFirstKey(cs: CharSequence, event: MotionEvent) {
    when (event.action) {
      ACTION_DOWN -> {
        // firstkeyを記録
        Timber.d("set firstkey $cs x:${event.x} y:${event.y}")
        firstText = cs.toString()
      }
    }
  }

  // TODO: プレビュー実装
  private fun onHoverSecondKey(event: MotionEvent, cs: CharSequence) {
    Timber.d("preview $firstText$cs")
  }

  private fun onTouchSecondKey(cs: CharSequence) {
    // firstkey + secondkeyをcommit
    currentInputConnection.commitText("$cs$firstText", 1)
    binding.firstKeys.alpha = 1f
  }

  // タッチイベントの座標からSecondKeyを計算
  private fun getSecondKeyFromTouchEvent(event: MotionEvent): String {
    for (i in secondKeys.indices) {
      val child = secondViewManager.getChildAt(i) ?: continue
      if (child.inBounds(event.rawX.toInt(), event.rawY.toInt())) {
        return secondKeys[i]
      }
    }
    return ""
  }
}
