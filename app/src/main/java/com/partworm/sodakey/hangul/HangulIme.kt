package com.partworm.sodakey.hangul

import com.partworm.sodakey.ime.Ime
import java.util.*

class HangulIme : Ime {

  var onEnterListener = {}
  var onComposingStringChangeListener = noOp
  var onCompositionCompleteListener = noOp

  private val stateEmpty = StateEmpty()
  private val stateConsonant = StateConsonant()
  private val stateVowel = StateVowel()
  private val stateIncomplete = StateIncomplete()
  private var state: State = stateEmpty

  val isConsonantState: Boolean
    get() = state === stateConsonant

  val isEmpty: Boolean
    get() = state === stateEmpty

  fun offerConsonant(con: Int) {
    state.offerConsonant(con)
    onComposingStringChangeListener(state.toString())
  }

  fun offerVowel(vowel: Int) {
    state.offerVowel(vowel)
    onComposingStringChangeListener(state.toString())
  }

  override fun offerEnter() {
    onEnterListener()
  }

  override fun offerBackspace() {
    state.offerBackspace()
    onComposingStringChangeListener(state.toString())
  }

  override fun offerString(str: String) {
    complete()
    onCompositionCompleteListener(str)
  }

  fun dismiss() {
    stateEmpty.reset()
  }

  private fun complete() {
    onCompositionCompleteListener(state.toString())
    stateEmpty.reset()
  }

  internal interface State {
    fun offerConsonant(con: Int)
    fun offerVowel(vowel: Int)
    fun offerBackspace()
  }

  internal inner class StateEmpty : State {

    fun reset() {
      state = this
    }

    override fun offerConsonant(con: Int) {
      stateConsonant.reset(con)
    }

    override fun offerVowel(vowel: Int) {
      stateVowel.reset(vowel)
    }

    override fun offerBackspace() {}

    override fun toString(): String {
      return ""
    }

  }

  internal inner class StateConsonant : State {

    private var con = 0

    fun reset(con: Int) {
      this.con = con
      state = this
    }

    override fun offerConsonant(con: Int) {
      complete()
      stateConsonant.reset(con)
    }

    override fun offerVowel(vowel: Int) {
      stateIncomplete.reset(con, vowel, LinkedList())
    }

    override fun offerBackspace() {
      stateEmpty.reset()
    }

    override fun toString(): String {
      return HangulUtil.consonantToString(con)
    }

  }

  internal inner class StateVowel : State {

    private var vowel = 0

    fun reset(vowel: Int) {
      this.vowel = vowel
      state = this
    }

    override fun offerConsonant(con: Int) {
      complete()
      stateConsonant.reset(con)
    }

    override fun offerVowel(vowel: Int) {
      complete()
      stateVowel.reset(vowel)
    }

    override fun offerBackspace() {
      stateEmpty.reset()
    }

    override fun toString(): String {
      return HangulUtil.vowelToString(vowel)
    }

  }

  internal inner class StateIncomplete : State {

    private var onset = 0
    private var nucleus = 0
    private var codas: LinkedList<Int> = LinkedList()

    fun reset(onset: Int, nucleus: Int, codas: LinkedList<Int>) {
      this.onset = onset
      this.nucleus = nucleus
      this.codas = codas
      state = this
    }

    override fun offerConsonant(con: Int) {
      codas.offerLast(con)
      if (HangulUtil.packIntoFinalConsonant(codas) == -1) {
        codas.pollLast()
        complete()
        stateConsonant.reset(con)
      }
    }

    override fun offerVowel(vowel: Int) {
      if (codas.isEmpty()) {
        complete()
        stateVowel.reset(vowel)
      }
      else {
        val nextOnset = codas.pollLast()
        complete()
        stateIncomplete.reset(nextOnset, vowel, LinkedList())
      }
    }

    override fun offerBackspace() {
      if (codas.isEmpty()) {
        stateConsonant.reset(onset)
      }
      else {
        codas.pollLast()
      }
    }

    override fun toString(): String {
      return HangulUtil.hangulToString(onset, nucleus, HangulUtil.packIntoFinalConsonant(codas))
    }

  }

  companion object {
    private val noOp = { _: String -> }
  }

}
