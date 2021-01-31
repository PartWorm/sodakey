package com.partworm.sodakey

import com.partworm.sodakey.keyaction.ConsonantTypingKeyAction
import com.partworm.sodakey.keyaction.KeyAction
import com.partworm.sodakey.keyaction.StringTypingKeyAction
import com.partworm.sodakey.keyaction.VowelTypingKeyAction
import java.util.*

class KeyLiteralParser(var str: String) {

  private val posStack = Stack<Int>()
  private var pos = 0
  private val chars: CharArray = str.toCharArray()

  private operator fun hasNext(): Boolean {
    return pos < chars.size
  }

  private fun ch(): Char {
    return if (pos >= chars.size) '\u0000' else chars[pos]
  }

  private fun push() {
    posStack.push(pos)
  }

  private fun pop() {
    pos = posStack.pop()
  }

  private fun popAndDrop() {
    posStack.pop()
  }

  private fun parseChar(ch: Char): Boolean {
    if (ch() != ch) {
      return false
    }
    ++pos
    return true
  }

  private fun parseString(str: String): Boolean {
    push()
    for (c in str.toCharArray()) {
      if (!parseChar(c)) {
        pop()
        return false
      }
    }
    popAndDrop()
    return true
  }

  private fun parseNum(): Int? {
    if (ch() < '0' || ch() > '9') {
      return null
    }
    var value = 0
    do {
      value = value * 10 + ch().toInt() - '0'.toInt()
      ++pos
    } while (ch() in '0'..'9')
    return value
  }

  private fun parseEnum(type: String, constr: (Int) -> KeyAction): KeyAction? {
    push()
    if (!parseString(type)) {
      pop()
      return null
    }
    if (!parseChar('(')) {
      pop()
      return null
    }
    val id = parseNum()
    if (id == null) {
      pop()
      return null
    }
    if (!parseChar(')')) {
      pop()
      return null
    }
    popAndDrop()
    return constr(id)
  }

  private fun parseConsonantEnum(): KeyAction? {
    return parseEnum("con") { con: Int -> ConsonantTypingKeyAction(con) }
  }

  private fun parseVowelEnum(): KeyAction? {
    return parseEnum("vowel") { vowel: Int -> VowelTypingKeyAction(vowel) }
  }

  private fun parseString(): KeyAction? {
    push()
    if (!parseChar('\'')) {
      pop()
      return null
    }
    val str = StringBuilder()
    while (hasNext() && ch() != '\'') {
      str.append(ch())
      ++pos
      if (parseChar('\\')) {
        if (!hasNext()) {
          pop()
          return null
        }
        str.append(ch())
        ++pos
      }
    }
    if (!parseChar('\'')) {
      pop()
      return null
    }
    popAndDrop()
    return StringTypingKeyAction(str.toString())
  }

  private fun parseLeafNode(): FlickNode? {
    val action = parseConsonantEnum() ?: parseVowelEnum() ?: parseString() ?: return null
    return FlickNode(action)
  }

  private fun parseInternalNode(): FlickNode? {
    push()
    if (!parseChar('(')) {
      return if (parseString("nil")) FlickNode.NULL else null
    }
    val root = parseLeafNode() ?: return null
    if (!parseChar(' ')) {
      pop()
      return null
    }
    val left = parseNode()
    if (left == null) {
      pop()
      return null
    }
    if (!parseChar(' ')) {
      pop()
      return null
    }
    val right = parseNode()
    if (right == null) {
      pop()
      return null
    }
    if (!parseChar(' ')) {
      pop()
      return null
    }
    val up = parseNode()
    if (up == null) {
      pop()
      return null
    }
    if (!parseChar(' ')) {
      pop()
      return null
    }
    val down = parseNode()
    if (down == null) {
      pop()
      return null
    }
    if (!parseChar(')')) {
      pop()
      return null
    }
    popAndDrop()
    return FlickNode(root.action, left, right, up, down)
  }

  private fun parseNode(): FlickNode? {
    return parseInternalNode() ?: parseLeafNode()
  }

  fun parse(): FlickNode {
    val result = parseNode()
    if (result == null) {
      throw Exception("An error occurred while parsing $str")
    }
    return result
  }

}