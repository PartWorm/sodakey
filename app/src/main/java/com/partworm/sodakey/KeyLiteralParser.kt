package com.partworm.sodakey

import com.partworm.sodakey.flick.FlickNode
import com.partworm.sodakey.hangul.keyaction.ConsonantTypingKeyAction
import com.partworm.sodakey.keyaction.KeyAction
import com.partworm.sodakey.keyaction.StringTypingKeyAction
import com.partworm.sodakey.hangul.keyaction.VowelTypingKeyAction
import com.partworm.sodakey.keyaction.EnterKeyAction
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

  private fun <T> pop(): T? {
    pos = posStack.pop()
    return null
  }

  private fun popAndDrop() {
    posStack.pop()
  }

  private fun parseChar(ch: Char): Boolean? {
    if (ch() != ch) {
      return null
    }
    ++pos
    return true
  }

  private fun parseString(str: String): Boolean? {
    push()
    for (c in str.toCharArray()) {
      parseChar(c) ?: return pop()
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
    parseString(type) ?: return pop()
    parseChar('(') ?: return pop()
    val id = parseNum() ?: return pop()
    parseChar(')') ?: return pop()
    popAndDrop()
    return constr(id)
  }

  private fun parseConsonantEnum(): KeyAction? {
    return parseEnum("con") { con: Int -> ConsonantTypingKeyAction(con) }
  }

  private fun parseVowelEnum(): KeyAction? {
    return parseEnum("vowel") { vowel: Int -> VowelTypingKeyAction(vowel) }
  }

  private fun parseEnter(): KeyAction? {
    parseString("enter") ?: return null
    return EnterKeyAction()
  }

  private fun parseString(): KeyAction? {
    push()
    parseChar('\'') ?: return pop()
    val str = StringBuilder()
    while (hasNext() && ch() != '\'') {
      str.append(ch())
      ++pos
      if (parseChar('\\') != null) {
        if (!hasNext()) {
          return pop()
        }
        str.append(ch())
        ++pos
      }
    }
    parseChar('\'') ?: return pop()
    popAndDrop()
    return StringTypingKeyAction(str.toString())
  }

  private fun parseNil(): FlickNode? {
    parseString("nil") ?: return null
    return FlickNode.NULL
  }

  private fun parseLeafNode(): FlickNode? {
    if (parseNil() != null) {
      return FlickNode.NULL
    }
    val action =
      parseConsonantEnum() ?:
      parseVowelEnum() ?:
      parseEnter() ?:
      parseString() ?:
      return null
    return FlickNode(action)
  }

  private fun parseInternalNode(): FlickNode? {
    push()
    parseChar('(') ?: return pop()
    val root = parseLeafNode() ?: return pop()
    parseChar(' ') ?: return pop()
    val left = parseNode() ?: return pop()
    parseChar(' ') ?: return pop()
    val right = parseNode() ?: return pop()
    parseChar(' ') ?: return pop()
    val up = parseNode() ?: return pop()
    parseChar(' ') ?: return pop()
    val down = parseNode() ?: return pop()
    parseChar(')') ?: return pop()
    popAndDrop()
    return FlickNode(root.action, left, right, up, down)
  }

  private fun parseNode(): FlickNode? {
    return parseInternalNode() ?: parseLeafNode()
  }

  fun parse(): FlickNode {
    return parseNode() ?: throw Exception("An error occurred while parsing $str")
  }

}