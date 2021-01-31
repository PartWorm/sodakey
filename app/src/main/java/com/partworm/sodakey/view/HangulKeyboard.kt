package com.partworm.sodakey.view

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.partworm.sodakey.KeyRearranger
import com.partworm.sodakey.ime.HangulIme

internal class HangulKeyboard : ConstraintLayout, Keyboard {

  private var service: InputMethodService? = null

  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
  constructor(context: Context?) : super(context) {}

  private val ime = HangulIme()
  override val keyRearranger = KeyRearranger(ime)

  fun registerChildKeys(root: ViewGroup) {
    for (i in 0 until root.childCount) {
      val child = root.getChildAt(i)
      if (child is ViewGroup) {
        registerChildKeys(child)
      } else if (child is KeyView) {
        child.keyboard = this
      }
    }
  }

  override fun offerBackspace() {
    val conn = service!!.currentInputConnection
    if (ime.isEmpty) {
      conn.deleteSurroundingText(1, 0)
    } else {
      ime.offerBackspace()
    }
  }

  override fun setService(service: InputMethodService) {
    ime.setOnComposingStringChangeListener { s: String? ->
      val conn = service.currentInputConnection
      conn.setComposingText(s, 1)
    }
    ime.setOnCompositionCompleteListener { s: String? ->
      val conn = service.currentInputConnection
      conn.commitText(s, 1)
    }
    this.service = service
    registerChildKeys(this)
  }

  override fun dismissComposingText() {
    ime.dismiss()
    service!!.currentInputConnection.finishComposingText()
  }

}