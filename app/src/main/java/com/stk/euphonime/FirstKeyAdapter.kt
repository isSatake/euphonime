package com.stk.euphonime

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FirstKeyAdapter(
  private val dataset: Array<String>,
  val onTouchKey: (cs: CharSequence, event: MotionEvent) -> Unit
) :
  RecyclerView.Adapter<FirstKeyAdapter.FirstKeyViewHolder>() {
  private val rythmDrawables = arrayOf(
    R.drawable.r32,
    R.drawable.r16,
    R.drawable.r8,
    R.drawable.r4,
    R.drawable.r2,
    R.drawable.r1
  )

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstKeyViewHolder {
    val firstKeyView = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.first_key_view, parent, false) as FirstKeyView
    return FirstKeyViewHolder(firstKeyView)
  }

  override fun onBindViewHolder(holder: FirstKeyViewHolder, position: Int) {
    with(holder.firstKeyView) {
      setOnTouchListener { _, event ->
        if (event.action == ACTION_DOWN) onTouchKey(dataset[position], event)
        false
      }
      setImageResource(rythmDrawables[position])
    }
  }

  override fun getItemCount() = dataset.size

  class FirstKeyViewHolder(val firstKeyView: FirstKeyView) : RecyclerView.ViewHolder(firstKeyView)
}
