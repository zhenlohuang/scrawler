package net.yidooo.scrawler.http

case class HttpRequest(url: String,
                       headers: Map[String, String] = Map.empty,
                       cookies: Map[String, String] = Map.empty,
                       charset: String = "utf-8")
