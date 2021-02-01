package com.partworm.sodakey

import com.partworm.sodakey.keyaction.KeyAction

class GestureAutomata(private val origin: FlickNode) {

  private var current: FlickNode = origin
  private var originalCenterX = 0
  private var originalCenterY = 0
  private var centerX = 0
  private var centerY = 0

  public val diffX: Int
    get() = centerX - originalCenterX

  public val diffY: Int
    get() = centerY - originalCenterY

  fun reset(x: Int, y: Int) {
    current = origin
    originalCenterX = x
    originalCenterY = y
    centerX = x
    centerY = y
  }

  fun move(x: Int, y: Int) {
    val dir = when {
      x < centerX - FLICK_THRESHOLD_X ->
        FlickDirection.LEFT
      x > centerX + FLICK_THRESHOLD_X ->
        FlickDirection.RIGHT
      y < centerY - FLICK_THRESHOLD_Y ->
        FlickDirection.UP
      y > centerY + FLICK_THRESHOLD_Y ->
        FlickDirection.DOWN
      else -> return
    }
    if (current.getNext(dir) !== FlickNode.NULL) {
      current = current.getNext(dir)
      centerX += dir.dx * FLICK_THRESHOLD_X * 2
      centerY += dir.dy * FLICK_THRESHOLD_Y * 2
    }
  }

  fun get(): KeyAction? {
    return current.action
  }

  companion object {
    private const val FLICK_THRESHOLD_X = 50
    private const val FLICK_THRESHOLD_Y = 30
  }

}