package net.yidooo.scrawler.parser.html

import org.scalatest.{FlatSpec, Matchers}


class JsoupParserTestSuite extends FlatSpec with Matchers {
  private val htmlText =
    """
      |<html>
      | <head>
      |  <title>异度部落格</title>
      | </head>
      | <body>
      |  <p>Test Page</p>
      |  <a href="http://www.yidooo.com">异度部落格</a>
      | </body>
      |</html>
    """.stripMargin

  "JsoupParser " should "parse title from html" in {
    val document = JsoupParser.parse(htmlText)
    assert(document.title == "异度部落格")
  }

  "JsoupParser " should "parse head from html" in {
    val document = JsoupParser.parse(htmlText)
    assert(document.head.innerHtml == "<title>异度部落格</title>")
  }

  "JsoupParser " should "parse body from html" in {
    val document = JsoupParser.parse(htmlText)
    assert(document.body.innerHtml == "<p>Test Page</p> \n<a href=\"http://www.yidooo.com\">异度部落格</a>")
  }

  "JsoupParser " should "parse text from html via CSS selector" in {
    val document = JsoupParser.parse(htmlText)
    assert(document.body.cssSelect("body > p").head.text == "Test Page")
    assert(document.body.cssSelect("body > a").head.attr("href") == "http://www.yidooo.com")
    assert(document.body.cssSelect("body > a").head.text == "异度部落格")
  }

  "JsoupParser " should "parse links from html via CSS selector" in {
    val document = JsoupParser.parse(htmlText)
    assert(document.body.links.head == "http://www.yidooo.com")
  }
}
