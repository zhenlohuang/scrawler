package net.yidooo.scrawler.downloader

import java.net.HttpCookie

import akka.actor.{Actor, ActorLogging, Props, UnhandledMessage}
import net.yidooo.scrawler.DownloaderConfig
import net.yidooo.scrawler.event.{CrawlRequestEvent, CrawlResponseEvent}
import net.yidooo.scrawler.model.{Request, Response}
import scalaj.http.{Http, HttpOptions}

class DownloaderActor(config: DownloaderConfig) extends Downloader with Actor with ActorLogging {
  override def download(request: Request): Response = {
    log.debug(s"Downloading ${request.url}")

    var http = Http(request.url)
      .headers(request.headers)
      .cookies(request.cookies.map({case (key, value) => new HttpCookie(key, value)}).toSeq)
      .timeout(config.connectionTimeout, config.readTimeout)
      .option(HttpOptions.followRedirects(true))
      .option(HttpOptions.allowUnsafeSSL)

    if(config.enableProxy()) {
      http = http.proxy(config.getProxy())
      if(config.hasProxyAuth()) {
        http = http.proxyAuth(config.proxyUser, config.proxyPassword)
      }
    }

    var retries = 0
    do {
      try {
        val response = http.asString

        val content = response.body
        val headers = response.headers
        val downloadStatus = response.code >= 200 && response.code < 300

        return Response(request, content, headers, downloadStatus, response.code)
      } catch {
        case ex: Exception =>
          log.warning(s"Failed to download ${request.url} with exception: ${ex.getMessage}")
          retries += 1
          if(retries <= config.retryTime) {
            log.warning(s"Retrying $retries time(s).")
          } else {
            throw ex
          }
      }
    } while(retries <= config.retryTime)

    //Should not reach here
    null
  }

  override def receive: Receive = {
    case CrawlRequestEvent(request) =>
      val response = download(request)
      sender() ! CrawlResponseEvent(response)
    case msg: UnhandledMessage =>
      log.error("Found unhandled message: " + msg)
  }
}

object DownloaderActor {
  def props(config: DownloaderConfig): Props = Props(new DownloaderActor(config))
}
