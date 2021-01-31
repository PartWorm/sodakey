package com.partworm.sodakey

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import com.partworm.sodakey.view.Keyboard

class Sodakey : InputMethodService() {

  private var currentKeyboard: Keyboard? = null

  override fun onCreateInputView(): View {
    val keyboard = layoutInflater.inflate(R.layout.hangul_keyboard, null)
    currentKeyboard = keyboard as Keyboard
    currentKeyboard!!.setService(this)
    return keyboard
  }

  override fun onUpdateSelection(
          oldSelStart: Int, oldSelEnd: Int, newSelStart: Int, newSelEnd: Int,
          candidatesStart: Int, candidatesEnd: Int) {
    if (currentKeyboard == null) return
    if (newSelStart == newSelEnd) {
      if (newSelStart == oldSelStart || newSelStart == oldSelStart + 1) {
        return
      }
    }
    currentKeyboard!!.dismissComposingText()
  }

  override fun onStartInput(attribute: EditorInfo, restarting: Boolean) {
    if (currentKeyboard == null) return
    currentKeyboard!!.dismissComposingText()
  }
}