package com.partworm.sodakey.ime

interface Ime {

  fun offerEnter()
  fun offerBackspace()
  fun offerString(str: String)

}