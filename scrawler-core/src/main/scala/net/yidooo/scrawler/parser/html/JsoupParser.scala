package net.yidooo.scrawler.parser.html

import org.jsoup.Jsoup
import scala.collection.JavaConverters._

object JsoupParser extends Parser {
  override def parse(text: String): Document = {
    JsoupDocument(Jsoup.parse(text))
  }

  override def parse(text: String, url: String): Document = {
    JsoupDocument(Jsoup.parse(text, url))
  }
}

case class JsoupDocument(private val  doc: org.jsoup.nodes.Document) extends Document {
  override def location: String = {
    doc.location()
  }

  override def root: Element = {
    JsoupElement(doc.getElementsByTag("html").first)
  }

  override def toHtml: String = doc.outerHtml()
}

case class JsoupElement(private val element: org.jsoup.nodes.Element) extends Element {
  override def tagName: String = element.tagName()

  override def parent: Option[Element] = Option(JsoupElement(element.parent()))

  override def children: Iterable[Element] = element.children.asScala.map(JsoupElement)

  override def siblings: Iterable[Element] = element.siblingElements.asScala.map(JsoupElement)

  override def attrs: Map[String, String] = element.attributes.asScala.map({attr => attr.getKey -> attr.getValue}).toMap

  override def hasAttr(attributeKey: String): Boolean = element.hasAttr(attributeKey)

  override def attr(attributeKey: String): String = element.attr(attributeKey)

  override def text: String = element.text()

  override def innerHtml: String = element.html()

  override def outerHtml: String = element.outerHtml()

  override def cssSelect(cssSelector: String): Iterable[Element] = element.select(cssSelector).asScala.map(JsoupElement)

  override def links: Iterable[String] = element.select("a").asScala.map(_.attr("href"))
}

