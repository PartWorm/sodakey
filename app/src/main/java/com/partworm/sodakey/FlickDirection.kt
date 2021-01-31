package com.partworm.sodakey

enum class FlickDirection(var dx: Int, var dy: Int) {
  LEFT(-1, 0),
  RIGHT(1, 0),
  UP(0, -1),
  DOWN(0, 1);
}