package com.partworm.sodakey.keyaction

import com.partworm.sodakey.ime.HangulIme
import com.partworm.sodakey.ime.Ime

class VowelTypingKeyAction(private val vowel: Int) : KeyAction {

  override fun accept(ime: Ime?) {
    (ime as HangulIme).offerVowel(vowel)
  }

}