package net.yidooo.scrawler.middleware
import net.yidooo.scrawler.model.Request

class DummyMiddleware extends RequestMiddleware {
  override def processRequest(request: Request): Request = request
}
