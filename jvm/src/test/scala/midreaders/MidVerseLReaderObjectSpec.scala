package edu.holycross.shot.mid.markupreaders

import edu.holycross.shot.mid.validator._
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._


class MidVerseLReaderObjectSpec extends FlatSpec {

  "The MidVerseLReaderDiplomatic object" should "recursively collect text from XML containers"  in  {
    val xml = """<l n="1">Ab <persName>ioue</persName> principium magno deduxit <persName>Aratus</persName></l>"""
    val urn = CtsUrn("urn:cts:latinLit:phi0881.phi003.bern88:1")
    val cn = CitableNode(urn,xml)
    // expect trailing space so xml elements are
    // not concatenated

    val expected = "Ab ioue principium magno deduxit Aratus"
    val actual = MidVerseLReaderDiplomatic.editedNode(cn)
    //assert(actual == expected)
    println(actual)
  }

  it should "thrown an exception if elements outside MID standard are found" in pending /*{
    val xml = "<div><watermark>Agamemnon</watermark></div>"
    try {
      MidProseABReader.diplomatic(xml)
    } catch {
      case e: Exception => assert(e.getMessage == "Unrecognized XML element: watermark")
    }
  }*/

  it should "ignore added content in diplomatic editions" in pending /*{
    val xml = "<div n=\"1\"><ab>Text 1<add>.1</add> version</ab></div>"
    // expect trailing space so xml elements are
    // not concatenated

    val expected = "Text 1 version "
    val actual = MidProseABReader.diplomatic(xml)
    assert(actual.replaceAll("[ ]+", " ") == expected)
  }*/


  it should "ignore include content deleted later in diplomatic editions" in  pending /*{
    val xml = "<div n=\"1\"><ab>Text 1<del>.1</del><add>.2</add> version</ab></div>"
    // expect trailing space so xml elements are
    // not concatenated

    val expected = "Text 1 .1 version "
    val actual = MidProseABReader.diplomatic(xml)
    assert(actual == expected)
  }
*/
}
