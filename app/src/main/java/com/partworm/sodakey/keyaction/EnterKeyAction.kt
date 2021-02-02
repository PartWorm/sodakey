package com.partworm.sodakey.keyaction

import com.partworm.sodakey.ime.Ime

class EnterKeyAction : KeyAction {

  override fun accept(ime: Ime) {
    ime.offerEnter()
  }

}