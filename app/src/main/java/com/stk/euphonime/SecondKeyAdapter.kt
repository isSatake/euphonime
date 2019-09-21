package com.stk.euphonime

import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_UP
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class SecondKeyAdapter(private val dataset: Array<String>, val onTouchKey: (cs: CharSequence) -> Unit) :
  RecyclerView.Adapter<SecondKeyAdapter.SecondKeyViewHolder>() {
  class SecondKeyViewHolder(val secondKeyView: SecondKeyView) : RecyclerView.ViewHolder(secondKeyView)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondKeyViewHolder {
    val secondKeyView = LayoutInflater
      .from(parent.context)
      .inflate(R.layout.second_key_view, parent, false) as SecondKeyView
    return SecondKeyViewHolder(secondKeyView)
  }

  override fun onBindViewHolder(holder: SecondKeyViewHolder, position: Int) {
    holder.secondKeyView.text = dataset[position]
  }

  override fun getItemCount() = dataset.size
}