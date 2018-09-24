package net.yidooo.scrawler.parser.html

trait Parser {
  def parse(text: String): Document
  def parse(text: String, url: String): Document
}
