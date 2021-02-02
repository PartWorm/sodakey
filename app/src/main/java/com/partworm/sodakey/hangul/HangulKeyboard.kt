package com.partworm.sodakey.hangul

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.partworm.sodakey.KeyRearranger
import com.partworm.sodakey.keyview.KeyView
import com.partworm.sodakey.keyboard.Keyboard

internal class HangulKeyboard : ConstraintLayout, Keyboard {

  private lateinit var service: InputMethodService

  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?) : super(context)

  private val ime = HangulIme()
  override val keyRearranger = KeyRearranger(ime)

  override fun init(service: InputMethodService) {
    ime.onEnterListener = {
      val conn = service.currentInputConnection
      conn.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
      conn.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
    }
    ime.onComposingStringChangeListener = { s: String ->
      val conn = service.currentInputConnection
      conn.setComposingText(s, 1)
    }
    ime.onCompositionCompleteListener = { s: String ->
      val conn = service.currentInputConnection
      conn.commitText(s, 1)
    }
    this.service = service
    registerChildKeys(this)
  }

  private fun registerChildKeys(root: ViewGroup) {
    for (i in 0 until root.childCount) {
      val child = root.getChildAt(i)
      if (child is ViewGroup) {
        registerChildKeys(child)
      }
      else if (child is KeyView) {
        child.registerKeyboard(this)
      }
    }
  }

  override fun offerBackspace() {
    val conn = service.currentInputConnection
    if (ime.isEmpty) {
      conn.deleteSurroundingText(1, 0)
    }
    else {
      ime.offerBackspace()
    }
  }

  override fun dismissComposingText() {
    ime.dismiss()
    service.currentInputConnection.finishComposingText()
  }

}