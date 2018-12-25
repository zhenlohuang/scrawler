package net.yidooo.scrawler.model

case class CrawlResponse(request: CrawlRequest,
                         rawText: String,
                         downloadStatus: Boolean,
                         statusCode: Int) {
  val url = request.url
}
