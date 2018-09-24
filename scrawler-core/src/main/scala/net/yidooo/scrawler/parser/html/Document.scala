package net.yidooo.scrawler.parser.html

trait Document {
  def location: String
  def root: Element
  def title: String = root.cssSelect("title").headOption.fold("")(_.text.trim)
  def head: Element = root.cssSelect("head").head
  def body: Element = root.cssSelect("body").head

  def toHtml: String
}
