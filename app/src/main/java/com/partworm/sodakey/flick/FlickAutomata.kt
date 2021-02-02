package com.partworm.sodakey.flick

import com.partworm.sodakey.keyaction.KeyAction

class FlickAutomata(private val origin: FlickNode) {

  lateinit var onFlickListener: () -> Unit

  private var current: FlickNode = origin
  private var originalCenterX = 0
  private var originalCenterY = 0
  private var centerX = 0
  private var centerY = 0

  val diffX: Int
    get() = centerX - originalCenterX

  val diffY: Int
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
      onFlickListener()
      current = current.getNext(dir)
      centerX += dir.dx * FLICK_THRESHOLD_X * 2
      centerY += dir.dy * FLICK_THRESHOLD_Y * 2
    }
  }

  fun get(): KeyAction {
    return current.action
  }

  companion object {
    private const val FLICK_THRESHOLD_X = 40
    private const val FLICK_THRESHOLD_Y = 30
  }

}