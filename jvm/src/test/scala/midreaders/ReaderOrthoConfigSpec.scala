package edu.holycross.shot.mid.markupreaders

import edu.holycross.shot.mid.validator._
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.cex._

import better.files._
import File._
import java.io.{File => JFile}
import better.files.Dsl._


class ReaderOrthoSpec extends FlatSpec {

  val repoPath = "jvm/src/test/resources/simplelatin"
  val readerMap : Map[String, Vector[MidMarkupReader]] = Map(
    "MidProseABDiplomatic" ->   Vector(MidProseABDiplomatic)
  )
  val orthoMap : Map[String, MidOrthography] = Map(
    "Latin23" -> Latin23
  )


  "An EditorsRepo" should "be able to configure readers by convention" in {
    val repo = EditorsRepo(repoPath, readerMap, orthoMap)
    val textUrn = CtsUrn("urn:cts:chant:massordinary.sg359.text_xml:")
    val pairing =  repo.readers.filter(_.urn ==  textUrn)
    // Expect only one reader for this text:
    assert(pairing(0).readers.size == 1)
    assert(pairing(0).readers(0).toString.contains("edu.holycross.shot.mid.markupreaders.MidProseABDiplomatic"))
  }



  it should "be able to configure orthographies by convention" in {
    val repo = EditorsRepo(repoPath, readerMap, orthoMap)
    val orthos = repo.orthographies
    assert(orthos.size == 1)
    val expectedText = CtsUrn("urn:cts:chant:massordinary.sg359.text_xml:")
    assert(orthos(0).urn == expectedText)
    val expectedOrtho = "edu.holycross.shot.mid.validator.Latin23"
    assert(orthos(0).orthography.toString.contains(expectedOrtho))
  }
}
