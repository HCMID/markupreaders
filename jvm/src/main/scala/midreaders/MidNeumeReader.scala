package edu.holycross.shot.mid.markupreaders

import edu.holycross.shot.mid.validator._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.xmlutils._
import scala.xml._

/** Reads MID texts editing neumes in TEI markup.
*
* @param editionType Type of edition to read.
*/
case class MidNeumeReader(applicableType: MidEditionType) extends MidMarkupReader {
  require(recognizedTypes.contains(applicableType), "Unrecognized edition type: " + applicableType)

  /** Vector of all recognized editionTypes.
  * In this release, the only type recognized
  * is the [MidDiplomaticEdition].
  */
  def recognizedTypes: Vector[MidEditionType] =  MidNeumeReader.recognizedTypes

  /** Specific edition type to apply. */
  def editionType: MidEditionType = applicableType

  /** Create a plain-text citable edition of type editionType
  * in CEX format from source string `archival`.
  *
  * @param archival Archival source text.
  * @param srcUrn URN for archival source text.
  */
  def editedNodeCex(cn: CitableNode): String = {
    val archival = cn.text
    val srcUrn = cn.urn
    val cex = StringBuilder.newBuilder
    //val editedUrn = srcUrn.dropVersion.addVersion(editionType.versionId)
    val editedUrn = srcUrn.dropVersion.addVersion( srcUrn.version + "_" + editionType.versionId)
    cex.append(editedUrn + "#")
    editionType match {
      case MidDiplomaticEdition => cex.append(MidNeumeReader.diplomatic(srcUrn, archival))
    }
    cex.toString
  }


  def editedNode(cn: CitableNode): CitableNode = {
    val archival = cn.text
    val srcUrn = cn.urn
    val cex = StringBuilder.newBuilder
    //val editedUrn = srcUrn.dropVersion.addVersion(editionType.versionId)
    val editedUrn = srcUrn.dropVersion.addVersion( srcUrn.version + "_" + editionType.versionId)
    cex.append(editedUrn + "#")
    editionType match {
      case MidDiplomaticEdition => cex.append(MidNeumeReader.diplomatic(srcUrn, archival))
    }
    CitableNode(editedUrn, cex.toString)
  }
}

/**  Implementation of MID model for prose texts encoded
* with terminal citation units in TEI `ab` elements.
*/
object MidNeumeReader {

  def readers : Vector[MidNeumeReader] = {
    val readerList = for (ed <- recognizedTypes) yield {
      MidNeumeReader(ed)
    }
    readerList
  }

  /** Vector of all recognized editionTypes.
  * In this release, we implement only the [MidDiplomaticEdition]
  * type.
  */
  def recognizedTypes: Vector[MidEditionType]= {
    Vector(MidDiplomaticEdition)
  }

  /** Generate pure diplomatic edition in CEX format.
  *
  * @param xml Archival source in MID-compliant TEI.
  * @param src CtsUrn of archival source edition.
  */
  def diplomatic(src: CtsUrn, xml: String) : String = {
    val root  = XML.loadString(xml)
    collectDiplomatic(root,"")
  }

  /** Recursively collect diplomatic text readings from
  * a given XML node.
  *
  * @param n Node to read.
  * @param s Any previously accumulated readings that subsequent
  * readings should be added to.
  */
  def collectDiplomatic(n: xml.Node, s: String): String = {
    val txt =  StringBuilder.newBuilder
    n match {

      case t: xml.Text =>  {
        val cleaner = t.toString().trim
        if (cleaner.nonEmpty){
          txt.append(cleaner + " ")
        }
      }

      case e: xml.Elem =>  {
        val appnd = addDiplomaticTextFromElement(e)
        appnd match {
          case None => {
            for (chld <- e.child) {
             txt.append(collectDiplomatic(chld, txt.toString))
            }
          }
          case _ => txt.append(appnd.get)
        }
     }
    }

    txt.toString
  }

  /** Determine what text to extract from a single XML element.
  *  Depending on the semantics of this element in MID
  * dipomatic markup, we return either the text content of the
  * element, or, if no text content should be extracted,
  * a single space to isolate this markup from any preceding
  * or following content, or if the element is a container
  * that should continue to be recursively analyzed, None.
  *
  * @param el Parsed XML element.
  */
  def addDiplomaticTextFromElement(el: xml.Elem): Option[String] = {
    el.label match {

      // Stop reading at any of these elements:
      case "teiHeader" => Some(" ")
      case "add" => Some(" ")
      case "expan" => Some(" ")
      case "corr" => Some(" ")
      case "ref" => Some(" ")
      case "reg" => Some(" ")


      // Read text content from these elements:
      case "abbr" => Some(el.text)
      case "cit" => Some(el.text)
      case "del" =>  Some(el.text)
      case "unclear" => Some(el.text)
      case "sic" => Some(el.text)
      case "orig" => Some(el.text)

      // Recursively read all text content wrapped in `w`
      case "w" => Some(TextReader.collectText(el))


      // Continue hierarchically descending reading
      // of these elements:
      case "ab" => None
      case "div" => None
      case "choice" => None
      case "q" => None


      case elementName: String => throw new Exception("Unrecognized XML element: " + elementName)
    }
  }


}
