package net.yidooo.scrawler.http

trait HttpClient {
  def doGet(request: HttpRequest): HttpResponse
}
