package edu.holycross.shot.mid.markupreaders

import edu.holycross.shot.mid.validator._
import org.scalatest.FlatSpec

import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._


class ReadersPairingSpec extends FlatSpec {

  "A ReadersPairing" should "have a vector of readers" in {
    val eins121 = CtsUrn("urn:cts:chant:massordinary.eins121:")
    val readers:  Vector[MidMarkupReader] = Vector(MidProseABDiplomatic)

    val pairings = ReadersPairing(eins121, readers)

    assert (pairings.readers == readers)
  }
}
