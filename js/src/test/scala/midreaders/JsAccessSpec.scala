package edu.holycross.shot.mid.markupreaders
import edu.holycross.shot.mid.validator._
import edu.holycross.shot.cite._
import org.scalatest.FlatSpec


class JsAccessSpec extends FlatSpec {

  "The MID validator library"  should "expose image managers" in pending /*{
    val mgr = ImageManager()
    val defaultICT = "http://www.homermultitext.org/ict2/"
    assert(mgr.ictBase ==  defaultICT)
  }
*/
  it should "expose edition type" in {
    assert (MidDiplomaticEdition.label == "diplomatic")
    assert (MidScribalEdition.label == "scribal")
  }
  it should "expose orthography systems" in  {
    val expectedTypes = Vector(PraenomenToken, PunctuationToken, LexicalToken, NumericToken)
    assert(expectedTypes == Latin23.tokenCategories)
  }

  it should "expose orthography pairings" in  {
    val expectedTypes = Vector(PraenomenToken, PunctuationToken, LexicalToken, NumericToken)
    val pairing = OrthoPairing(CtsUrn("urn:cts:omar:stoa0179.stoa001:"), Latin23)
    assert(pairing.orthography.tokenCategories == expectedTypes)
  }

  it should "expose common token types" in {
    assert(PunctuationToken.name == "punctuation")
  }

  it should "expose concrete methods of orthography trait" in {
    val cps = Latin23.strToCps("abc")
    val expected = Vector(97, 98, 99)
    assert(cps == expected)

    assert(Latin23.validString("ius belli"))
  }

  it should "expose MidToken" in {
    val tkn = MidToken(CtsUrn("urn:cts:test:none.demoval:1"),"bogus", None)
    assert(tkn.string == "bogus")
  }

  it should "expose TokenHistogram" in  pending /*{
    val tkn = MidToken(CtsUrn("urn:cts:test:none.demoval:1"),"bogus", None)
    val tokenVector = Vector(tkn)
    val hist = TokenHistogram(tokenVector)
    assert(hist.tokens.size == 1)
  }*/

  it should "expose concrete methods of the markup reader trait" in pending

}
