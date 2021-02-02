package com.partworm.sodakey

import com.partworm.sodakey.hangul.HangulIme
import com.partworm.sodakey.ime.Ime
import com.partworm.sodakey.hangul.keyaction.ConsonantTypingKeyAction
import com.partworm.sodakey.hangul.keyaction.VowelTypingKeyAction
import com.partworm.sodakey.keyview.FlickKeyView
import java.util.*

class KeyRearranger(private val ime: Ime) {

  private var count = 0
  private val keys: MutableList<FlickKeyView> = ArrayList()

  fun press(keyView: FlickKeyView) {
    keys.add(keyView)
    ++count
  }

  fun releaseOne() {
    --count
    if (count == 0) {
      if (keys.size >= 2 && ime is HangulIme) {
        if (ime.isConsonantState) {
          if (keys[0].get() is ConsonantTypingKeyAction && keys[1].get() is VowelTypingKeyAction) {
            Collections.swap(keys, 0, 1)
          }
        }
        else if (keys[0].get() is VowelTypingKeyAction && keys[1].get() is ConsonantTypingKeyAction) {
          Collections.swap(keys, 0, 1)
        }
      }
      for (key in keys) {
        key.get().accept(ime)
      }
      keys.clear()
    }
  }

}