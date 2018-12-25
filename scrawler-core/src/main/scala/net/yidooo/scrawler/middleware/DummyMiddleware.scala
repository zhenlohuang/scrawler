package net.yidooo.scrawler.middleware
import net.yidooo.scrawler.model.CrawlRequest

class DummyMiddleware extends RequestMiddleware {
  override def processRequest(request: CrawlRequest): CrawlRequest = request
}
