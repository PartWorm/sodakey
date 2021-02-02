package com.partworm.sodakey.flick

import com.partworm.sodakey.ime.Ime
import com.partworm.sodakey.keyaction.KeyAction
import java.util.*

class FlickNode(val action: KeyAction) {

  private val nextNodes = EnumMap<FlickDirection, FlickNode>(FlickDirection::class.java)

  constructor(action: KeyAction, left: FlickNode, right: FlickNode, up: FlickNode, down: FlickNode) : this(action) {
    nextNodes[FlickDirection.LEFT] = left
    nextNodes[FlickDirection.RIGHT] = right
    nextNodes[FlickDirection.UP] = up
    nextNodes[FlickDirection.DOWN] = down
  }

  fun getNext(dir: FlickDirection): FlickNode {
    return nextNodes.getOrDefault(dir, NULL)
  }

  companion object {
    val NULL = FlickNode(object : KeyAction {
      override fun accept(_ime: Ime) {
      }
    })
  }

}