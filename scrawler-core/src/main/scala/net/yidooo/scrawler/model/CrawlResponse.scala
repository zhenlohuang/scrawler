package net.yidooo.scrawler.model

import net.yidooo.scrawler.http.HttpResponse

case class CrawlResponse(request: CrawlRequest,
                         httpResponse: HttpResponse) {
  val url = request.url
  val httpRequest = request.httpRequest
  val downloadStatus = httpResponse.statusCode >= 200 && httpResponse.statusCode < 300
}
