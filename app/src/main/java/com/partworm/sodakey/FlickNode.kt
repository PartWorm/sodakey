package com.partworm.sodakey

import com.partworm.sodakey.keyaction.KeyAction
import java.util.*

class FlickNode(var action: KeyAction?) {

  private val nextNodes = EnumMap<FlickDirection, FlickNode>(FlickDirection::class.java)

  constructor(action: KeyAction?, left: FlickNode, right: FlickNode, up: FlickNode, down: FlickNode) : this(action) {
    nextNodes[FlickDirection.LEFT] = left
    nextNodes[FlickDirection.RIGHT] = right
    nextNodes[FlickDirection.UP] = up
    nextNodes[FlickDirection.DOWN] = down
  }

  fun getNext(dir: FlickDirection): FlickNode {
    return nextNodes.getOrDefault(dir, NULL)
  }

  companion object {
    val NULL = FlickNode(null)
  }

}