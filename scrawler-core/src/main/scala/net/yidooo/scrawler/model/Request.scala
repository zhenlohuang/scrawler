package net.yidooo.scrawler.model

case class Request(url: String,
                   headers: Map[String, String] = Map.empty,
                   cookies: Map[String, String] = Map.empty,
                   charset: String = "utf-8",
                   skip: Boolean = false)

object Request {
  def of(url: String): Request = {
    Request(url)
  }
}