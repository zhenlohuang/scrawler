package net.yidooo.scrawler.model

case class CrawlRequest(url: String,
                        headers: Map[String, String] = Map.empty,
                        cookies: Map[String, String] = Map.empty,
                        charset: String = "utf-8",
                        skip: Boolean = false)

object CrawlRequest {
  def of(url: String): CrawlRequest = {
    CrawlRequest(url)
  }
}