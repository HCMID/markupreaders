package edu.holycross.shot.mid.markupreaders

import edu.holycross.shot.mid.validator._
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

class MidProseABReaderSpec extends FlatSpec {

  val reader = MidProseABReader(MidDiplomaticEdition)

  "An MidProseABReader" should "create diplomatic CEX from a parsed node" in pending
  /*{
    val xml = "<div n=\"1\"><ab>Text 1<del>.1</del><add>.2</add> version</ab></div>"
    val urn =CtsUrn("urn:cts:mid:unittests.1.xml:1")

    val actual = reader.editedNodeCex(urn, xml)
    val expected = "urn:cts:mid:unittests.1.xml_dipl:1#Text 1 .1 version"
    assert (actual.trim == expected)
  }*/

  it should "compose CEX for a node" in pending /* {
    val srcCex = "urn:cts:tests:dummy.txt1.v1:1#<div n=\"1\"><ab>Text 1<del>.1</del><add>.2</add> version</ab></div>"
    val expected = "urn:cts:tests:dummy.txt1.v1_dipl:1#Text 1 .1 version"
    assert(reader.editedNodeCex(srcCex).trim == expected)
  }*/
}
