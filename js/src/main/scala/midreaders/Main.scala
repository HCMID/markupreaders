package edu.holycross.shot.mid.markupreaders
import edu.holycross.shot.mid.validator._

import scala.scalajs.js

object Main extends js.JSApp {
  def main(): Unit = {

    println("Testing readers and orthography in a main object in JS branch.")
    val expectedTypes = Vector(PraenomenToken, PunctuationToken, LexicalToken, NumericToken)
    print("Test Latin23 orthography's token types: works? ")
    println(expectedTypes == Latin23.tokenCategories)
  }
}
