package com.partworm.sodakey.keyview

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.partworm.sodakey.flick.FlickAutomata
import com.partworm.sodakey.KeyLiteralParser
import com.partworm.sodakey.R
import com.partworm.sodakey.keyaction.KeyAction

class FlickKeyView : KeyView {

  private lateinit var automata: FlickAutomata

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?) : super(context)

  override fun init(
    context: Context,
    attrs: AttributeSet?
  ) {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.KeyView)
    val src = typedArray.getString(R.styleable.KeyView_key_src)!!
    typedArray.recycle()
    automata = FlickAutomata(KeyLiteralParser(src).parse())
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val effect = VibrationEffect.createOneShot(50, 90)
      automata.onFlickListener = {
        vibrator.vibrate(effect)
      }
    }
    else {
      automata.onFlickListener = {
        vibrator.vibrate(50)
      }
    }
  }

  fun get(): KeyAction {
    return automata.get()
  }

  override fun onTouch(v: View, e: MotionEvent): Boolean {
    super.onTouch(v, e)
    return when (e.action) {
      MotionEvent.ACTION_DOWN -> {
        keyboard.keyRearranger.press(this)
        automata.reset(e.rawX.toInt(), e.rawY.toInt())
        true
      }
      MotionEvent.ACTION_MOVE -> {
        automata.move(e.rawX.toInt(), e.rawY.toInt())
        v.translationX = automata.diffX.toFloat() / 5
        v.translationY = automata.diffY.toFloat() / 5
        true
      }
      MotionEvent.ACTION_UP -> {
        keyboard.keyRearranger.releaseOne()
        v.translationX = 0f
        v.translationY = 0f
        false
      }
      else -> {
        false
      }
    }
  }

}