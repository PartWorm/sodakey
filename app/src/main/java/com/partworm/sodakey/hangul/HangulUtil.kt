package com.partworm.sodakey.hangul

object HangulUtil {

  private val codaComposedBy = listOf(
    emptyList(), listOf(0), listOf(1), listOf(0, 9), listOf(2),
    listOf(2, 12), listOf(2, 18), listOf(3), listOf(5), listOf(5, 0),
    listOf(5, 6), listOf(5, 7), listOf(5, 9), listOf(5, 16), listOf(5, 17),
    listOf(5, 18), listOf(6), listOf(7), listOf(7, 9), listOf(9),
    listOf(10), listOf(11), listOf(12), listOf(14), listOf(15), listOf(16),
    listOf(17), listOf(18)
  )

  private val con2Uni = intArrayOf(
    0, 1, 3, 6, 7, 8, 16, 17, 18, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29
  )

  fun packIntoFinalConsonant(cons: List<Int>?): Int {
    return codaComposedBy.indexOf(cons)
  }

  fun consonantToString(con: Int): String {
    return (0x3131 + con2Uni[con]).toChar().toString()
  }

  fun vowelToString(vowel: Int): String {
    return (0x314F + vowel).toChar().toString()
  }

  fun hangulToString(onset: Int, nuc: Int, coda: Int): String {
    return (0xAC00 + (onset * 21 + nuc) * 28 + coda).toChar().toString()
  }

}