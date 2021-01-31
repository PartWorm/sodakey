package com.partworm.sodakey.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.partworm.sodakey.GestureAutomata
import com.partworm.sodakey.KeyLiteralParser
import com.partworm.sodakey.R
import com.partworm.sodakey.keyaction.KeyAction

class FlickKeyView : KeyView {

  private var automata: GestureAutomata? = null

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
  constructor(context: Context?) : super(context) {}

  override fun init(context: Context, attrs: AttributeSet?) {
    super.init(context, attrs)
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.KeyView)
    val src = typedArray.getString(R.styleable.KeyView_key_src)
    typedArray.recycle()
    automata = GestureAutomata(KeyLiteralParser(src).parse())
  }

  fun get(): KeyAction? {
    return automata!!.get()
  }

  override fun onTouch(v: View, e: MotionEvent): Boolean {
    super.onTouch(v, e)
    return when (e.action) {
      MotionEvent.ACTION_DOWN -> {
        keyboard?.keyRearranger?.press(this)
        automata!!.reset(e.rawX.toInt(), e.rawY.toInt())
        true
      }
      MotionEvent.ACTION_MOVE -> {
        automata!!.move(e.rawX.toInt(), e.rawY.toInt())
        true
      }
      MotionEvent.ACTION_UP -> {
        keyboard?.keyRearranger?.releaseOne()
        false
      }
      else -> {
        false
      }
    }
  }
}