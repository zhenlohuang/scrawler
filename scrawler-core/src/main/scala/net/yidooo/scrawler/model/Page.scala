package net.yidooo.scrawler.model

import net.yidooo.scrawler.parser.html.{Document, JsoupParser}

case class Page(rawText: String, url: String) {
  lazy val html = JsoupParser.parse(rawText, url)
}
