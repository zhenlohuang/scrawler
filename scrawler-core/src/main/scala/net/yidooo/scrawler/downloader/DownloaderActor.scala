package net.yidooo.scrawler.downloader

import akka.actor.{Actor, ActorLogging, Props, UnhandledMessage}
import net.yidooo.scrawler.DownloaderConfig
import net.yidooo.scrawler.event.{CrawlRequestEvent, CrawlResponseEvent}
import net.yidooo.scrawler.http.{HttpClient, HttpRequest}
import net.yidooo.scrawler.http.impl.ApacheHttpClient
import net.yidooo.scrawler.model.{CrawlRequest, CrawlResponse}

class DownloaderActor(config: DownloaderConfig) extends Downloader with Actor with ActorLogging {
  override def download(request: CrawlRequest): CrawlResponse = {
    log.debug(s"Downloading ${request.url}")

    val httpClient: HttpClient = new ApacheHttpClient(config.httpClientConfig)

    var retries = 0
    do {
      try {
        val httpRequest = HttpRequest(request.url, request.headers, request.cookies, request.charset)
        val httpResponse = httpClient.doGet(httpRequest)

        val downloadStatus = httpResponse.statusCode >= 200 && httpResponse.statusCode < 300
        return CrawlResponse(request, httpResponse.body, downloadStatus, httpResponse.statusCode)
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
