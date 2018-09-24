package net.yidooo.scrawler.middleware

import net.yidooo.scrawler.model.{Request, Response}

trait RequestMiddleware {
  def processRequest(request: Request): Request
}

trait ResponseMiddleware {
  def processResponse(response: Response): Request
}


