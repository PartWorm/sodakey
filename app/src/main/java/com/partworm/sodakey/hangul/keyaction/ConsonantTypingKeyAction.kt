package com.partworm.sodakey.hangul.keyaction

import com.partworm.sodakey.hangul.HangulIme
import com.partworm.sodakey.ime.Ime
import com.partworm.sodakey.keyaction.KeyAction

class ConsonantTypingKeyAction(private val con: Int): KeyAction {

  override fun accept(ime: Ime) {
    (ime as HangulIme).offerConsonant(con)
  }

}