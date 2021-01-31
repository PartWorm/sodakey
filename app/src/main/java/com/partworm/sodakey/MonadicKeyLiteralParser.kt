package com.partworm.sodakey

typealias Retn<T> = MonadicKeyLiteralParser.ReturnParser<T>
typealias Many<T> = MonadicKeyLiteralParser.ManyParser<T>
typealias Ch = MonadicKeyLiteralParser.CharParser
typealias Str = MonadicKeyLiteralParser.StringParser
typealias Sat = MonadicKeyLiteralParser.SatisfyCharParser

class MonadicKeyLiteralParser(val str: String) {

  val any = AnyCharParser()
  val charLiteral = (Ch('\\') then any) or Sat { c -> c != '\\' }
  val stringLiteral = Ch('\'') then Many(charLiteral) bind { s -> Ch('\'') then Retn(s) }

  fun parse() {

  }

  inner class AnyCharParser() : Parser<Char> {

    override fun invoke(pos: Int): ParseResult<Char> {
      return if (pos >= str.length) null else Pair(str[pos], pos + 1)
    }
  }

  inner class ReturnParser<T>(val t: T) : Parser<T> {

    override fun invoke(pos: Int): ParseResult<T> {
      return Pair(t, pos)
    }
  }

  inner class ManyParser<T>(val p: Parser<T>) : Parser<List<T>> {

    override fun invoke(pos: Int): ParseResult<List<T>> {
      val results = ArrayList<T>()
      while (true) {
        val result = p(pos) ?: break
        results.add(result.first)
      }
      return Pair(results, pos)
    }
  }

  inner class SatisfyCharParser(val f: (Char) -> Boolean) : Parser<Char> {

    override fun invoke(pos: Int): ParseResult<Char> {
      return if (pos >= str.length || f(str[pos])) null else Pair(str[pos], pos + 1)
    }
  }

  inner class CharParser(val c: Char) : Parser<Unit> {

    override fun invoke(pos: Int): ParseResult<Unit> {
      return if (pos >= str.length || str[pos] != c) null else Pair(Unit, pos + 1)
    }
  }

  inner class StringParser(val s: String) : Parser<Unit> {

    override fun invoke(pos: Int): ParseResult<Unit> {
      return if (str.substring(pos, pos + s.length) == s) Pair(Unit, pos + s.length) else null
    }
  }
}

typealias ParseResult<T> = Pair<T, Int>?
typealias Parser<T> = (Int) -> ParseResult<T>

infix fun <T, U> Parser<T>.bind(f: (T) -> Parser<U>): (Int) -> ParseResult<U> {
  return l@{ i ->
    val result = this(i) ?: return@l null
    f(result.first)(result.second)
  }
}

infix fun <T, U> Parser<T>.then(p: Parser<U>): (Int) -> ParseResult<U> {
  return l@{ i ->
    val result = this(i) ?: return@l null
    p(result.second)
  }
}

infix fun <T> Parser<T>.or(p: Parser<T>): (Int) -> ParseResult<T> {
  return { i -> this(i) ?: p(i) }
}