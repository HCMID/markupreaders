package edu.holycross.shot.mid.markupreaders

import edu.holycross.shot.mid.validator._
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._

class MidTeiReaderSpec extends FlatSpec {

  val reader = MidTeiReader(MidDiplomaticEdition)

  "An MidProseABReader" should "create diplomatic CEX from a parsed node" in {
    val xml = """<div type="section" n="1" xmlns:tei="http://www.tei-c.org/ns/1.0" xmlns="http://www.tei-c.org/ns/1.0"> <p>Non dubito fore plerosque, Attice, qui hoc genus scripturae leve et non satis dignum summorum virorum personis iudicent, cum relatum legent, quis musicam docuerit Epaminondam, aut in eius virtutibus commemorari, saltasse eum commode scienterque tibiis cantasse.</p> </div>"""
    val urn =CtsUrn("urn:cts:latinLit:stoa0588.stoa001.fleckeisen:praef.1.1#")

    val actual = reader.editedNodeCex(urn, xml)
    val expected = "urn:cts:latinLit:stoa0588.stoa001.fleckeisen_dipl:praef.1.1##Non dubito fore plerosque, Attice, qui hoc genus scripturae leve et non satis dignum summorum virorum personis iudicent, cum relatum legent, quis musicam docuerit Epaminondam, aut in eius virtutibus commemorari, saltasse eum commode scienterque tibiis cantasse."
    assert (actual.trim == expected)

  }

  it should "recognize all TEI elements in HC edition of Nepos" in {
    val neposCex = "jvm/src/test/resources/nepxml.cex"
    val neposXml = CorpusSource.fromFile(neposCex)
    val univocal = Corpus(neposXml.nodes.map(reader.editedNode(_)))
    val expectedSize = 720
    assert(expectedSize == univocal.size)

    val hannibalUrn = CtsUrn("urn:cts:latinLit:stoa0588.stoa001.fleckeisen_dipl:hannibal")
    val hannibal = univocal ~~ hannibalUrn
    val expectedHannibalSize = 62
    assert(hannibal.size == expectedHannibalSize)

  }


}
