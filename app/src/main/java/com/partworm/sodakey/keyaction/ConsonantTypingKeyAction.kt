package com.partworm.sodakey.keyaction

import com.partworm.sodakey.ime.HangulIme
import com.partworm.sodakey.ime.Ime

class ConsonantTypingKeyAction(private val con: Int): KeyAction {

  override fun accept(ime: Ime?) {
    (ime as HangulIme).offerConsonant(con)
  }
}