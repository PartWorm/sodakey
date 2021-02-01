package com.partworm.sodakey.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatButton

abstract class KeyView : AppCompatButton, OnTouchListener {

  public var keyboard: Keyboard? = null

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    init(context, attrs)
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    init(context, attrs)
  }

  constructor(context: Context?) : super(context) {}

  @SuppressLint("ClickableViewAccessibility")
  protected open fun init(context: Context, attrs: AttributeSet?) {
    setOnTouchListener(this)
  }

  override fun onTouch(v: View, e: MotionEvent): Boolean {
    return when (e.action) {
      MotionEvent.ACTION_DOWN -> {
        v.isPressed = true
        v.scaleX = 0.95f
        v.scaleY = 0.95f
        true
      }
      MotionEvent.ACTION_UP -> {
        v.isPressed = false
        v.scaleX = 1f
        v.scaleY = 1f
        false
      }
      else -> {
        false
      }
    }
  }

}