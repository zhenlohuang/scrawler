package net.yidooo.scrawler.model

import net.yidooo.scrawler.http.HttpRequest

case class CrawlRequest(httpRequest: HttpRequest,
                        skip: Boolean = false) {
  val url = httpRequest.url
}

object CrawlRequest {
  def of(url: String): CrawlRequest = {
    CrawlRequest(HttpRequest(url))
  }
}