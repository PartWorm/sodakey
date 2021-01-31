package com.partworm.sodakey.keyaction

import com.partworm.sodakey.ime.Ime

class StringTypingKeyAction(private val value: String) : KeyAction {

  override fun accept(ime: Ime?) {
    ime?.offerString(value)
  }

}