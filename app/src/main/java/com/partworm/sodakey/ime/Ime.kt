package com.partworm.sodakey.ime

interface Ime {
  fun offerBackspace()
  fun offerString(str: String)
}