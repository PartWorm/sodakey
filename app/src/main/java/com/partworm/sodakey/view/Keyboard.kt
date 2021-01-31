package com.partworm.sodakey.view

import android.inputmethodservice.InputMethodService
import com.partworm.sodakey.KeyRearranger

interface Keyboard {
  fun setService(service: InputMethodService)
  fun offerBackspace()
  fun dismissComposingText()
  val keyRearranger: KeyRearranger
}