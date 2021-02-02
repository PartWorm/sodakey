package com.partworm.sodakey.keyboard

import android.inputmethodservice.InputMethodService
import com.partworm.sodakey.KeyRearranger

interface Keyboard {

  fun init(service: InputMethodService)
  fun offerBackspace()
  fun dismissComposingText()

  val keyRearranger: KeyRearranger

}