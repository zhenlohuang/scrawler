package net.yidooo.scrawler.model

case class Response(request: Request,
                    rawText: String,
                    headers: Map[String, Seq[String]],
                    downloadStatus: Boolean,
                    statusCode: Int) {
  val url = request.url
}
