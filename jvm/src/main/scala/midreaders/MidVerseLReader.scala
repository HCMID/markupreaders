package edu.holycross.shot.mid.markupreaders

import edu.holycross.shot.mid.validator._
import edu.holycross.shot.cite._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.xmlutils._
import scala.xml._


object MidVerseLReaderDiplomatic extends MidMarkupReader {
  def editedNode(cn: CitableNode) : CitableNode = MidVerseLReader(MidDiplomaticEdition).editedNode(cn)
  def editionType = MidDiplomaticEdition
  def recognizedTypes = Vector(MidDiplomaticEdition)
}

/** Reads MID prose texts in TEI markup using `ab`
* element as terminal citation unit.
*
* @param editionType Type of edition to read.
*/
case class MidVerseLReader(applicableType: MidEditionType) extends MidMarkupReader {
  require(recognizedTypes.contains(applicableType), "Unrecognized edition type: " + applicableType)


  def  recognizedTypes: Vector[MidEditionType] = MidVerseLReader.recognizedTypes

  /** Implementation of function required by MidMarkupReader
  * trait specifying type of edition to create. */
  def editionType: MidEditionType = applicableType

  /** Create a plain-text citable node of type editionType
  * in CEX format from source string `archival`.
  *
  * @param archival Archival source text.
  * @param srcUrn URN for archival source text.
  */
  def editedNodeCex(cn: CitableNode): String = {
    val archival = cn.text
    val srcUrn = cn.urn
    val cex = StringBuilder.newBuilder
    val editedUrn = srcUrn.dropVersion.addVersion( srcUrn.version + "_" + editionType.versionId)
    cex.append(editedUrn + "#")
    editionType match {
      case MidDiplomaticEdition => cex.append(MidVerseLReader.diplomatic(archival))
    }
    cex.toString
  }

  def editedNode(cn: CitableNode): CitableNode = {
    val archival = cn.text
    val srcUrn = cn.urn
    val editedUrn = srcUrn.dropVersion.addVersion( srcUrn.version + "_" + editionType.versionId)

    val content = editionType match {
      case MidDiplomaticEdition => MidVerseLReader.diplomatic(archival)
    }
    CitableNode(editedUrn, content)
  }
}

/**  Implementation of MID model for prose texts encoded
* with terminal citation units in TEI `ab` elements.
*/
object MidVerseLReader {


  def readers : Vector[MidVerseLReader] = {
    val readerList = for (ed <- recognizedTypes) yield {
      MidVerseLReader(ed)
    }
    readerList.toVector
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
  def diplomatic(xml: String) : String = {
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


      // Read text content and append numeric sign marker,
      // Unicode 0x374 :
      case "num" =>  Some(el.text  + "ʹ")
      // Recursively read all text content wrapped in `w`
      case "w" => Some(TextReader.collectText(el))


      // Continue hierarchically descending reading
      // of these elements:
      case "l" => None


      case "choice" => None
      case "persName" => None
      case "placeName" => None
      case "q" => None
      case "foreign" => None
      case "seg" =>  None



      case elementName: String => throw new Exception("Unrecognized XML element: " + elementName)
    }
  }


}
