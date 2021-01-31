package com.partworm.sodakey.hangulutil

import java.util.*

object HangulUtil {
  private val codaComposedBy = Arrays.asList(emptyList(), Arrays.asList(0), Arrays.asList(1), Arrays.asList(0, 9), Arrays.asList(2),
          Arrays.asList(2, 12), Arrays.asList(2, 18), Arrays.asList(3), Arrays.asList(5), Arrays.asList(5, 0),
          Arrays.asList(5, 6), Arrays.asList(5, 7), Arrays.asList(5, 9), Arrays.asList(5, 16), Arrays.asList(5, 17),
          Arrays.asList(5, 18), Arrays.asList(6), Arrays.asList(7), Arrays.asList(7, 9), Arrays.asList(9),
          Arrays.asList(10), Arrays.asList(11), Arrays.asList(12), Arrays.asList(14), Arrays.asList(15), Arrays.asList(16),
          Arrays.asList(17), Arrays.asList(18)
  )
  private val con2Uni = intArrayOf(0, 1, 3, 6, 7, 8, 16, 17, 18, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29)
  fun packIntoFinalConsonant(cons: List<Int>?): Int {
    return codaComposedBy.indexOf(cons)
  }

  fun consonantToString(con: Int): String {
    return Character.toString((0x3131 + con2Uni[con]).toChar())
  }

  fun vowelToString(vowel: Int): String {
    return Character.toString((0x314F + vowel).toChar())
  }

  fun hangulToString(onset: Int, nuc: Int, coda: Int): String {
    return Character.toString((0xAC00 + (onset * 21 + nuc) * 28 + coda).toChar())
  }
}