package com.partworm.sodakey.keyview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatButton
import com.partworm.sodakey.keyboard.Keyboard

abstract class KeyView : AppCompatButton, OnTouchListener {

  lateinit var keyboard: Keyboard

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    init(context, attrs)
    setOnTouchListener(this)
  }

  constructor(context: Context?) : super(context)

  fun registerKeyboard(keyboard: Keyboard) {
    this.keyboard = keyboard
  }

  protected open fun init(context: Context, attrs: AttributeSet?) {}

  override fun onTouch(v: View, e: MotionEvent): Boolean {
    return when (e.action) {
      MotionEvent.ACTION_DOWN -> {
        v.isPressed = true
        true
      }
      MotionEvent.ACTION_UP -> {
        v.isPressed = false
        false
      }
      else -> {
        false
      }
    }
  }

}