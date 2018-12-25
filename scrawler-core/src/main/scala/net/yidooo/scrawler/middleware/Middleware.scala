package net.yidooo.scrawler.middleware

import net.yidooo.scrawler.model.{CrawlRequest, CrawlResponse}

trait RequestMiddleware {
  def processRequest(request: CrawlRequest): CrawlRequest
}

trait ResponseMiddleware {
  def processResponse(response: CrawlResponse): CrawlRequest
}


