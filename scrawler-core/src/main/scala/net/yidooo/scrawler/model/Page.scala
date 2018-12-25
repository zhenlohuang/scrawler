package net.yidooo.scrawler.model

import net.yidooo.scrawler.parser.html.JsoupParser

case class Page(rawText: String, url: String) {
  lazy val html = JsoupParser.parse(rawText, url)
}
