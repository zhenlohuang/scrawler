package net.yidooo.scrawler.parser.html

trait Element {
  def tagName: String
  def parent: Option[Element]
  def children: Iterable[Element]
//  def childNodes: Iterable[Node]
  def siblings: Iterable[Element]
//  def siblingNodes: Iterable[Node]
  def attrs: Map[String, String]
  def hasAttr(attributeKey: String): Boolean
  def attr(attributeKey: String): String
  def text: String
  def links: Iterable[String]
  def innerHtml: String
  def outerHtml: String
  def cssSelect(cssSelector: String): Iterable[Element]
}
