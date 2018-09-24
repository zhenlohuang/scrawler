package net.yidooo.scrawler

import akka.actor.{Actor, ActorLogging, Props, Timers, UnhandledMessage}
import akka.routing._
import net.yidooo.scrawler.downloader.DownloaderActor
import net.yidooo.scrawler.event._
import net.yidooo.scrawler.middleware.RequestMiddleware
import net.yidooo.scrawler.model._
import net.yidooo.scrawler.processor.{ItemProcessorActor, PageProcessorActor}


class Engine(config: ScrawlerConfig, requestMiddleware: RequestMiddleware)
  extends Actor with Timers with ActorLogging {

  private val downloaderRouter = context.actorOf(FromConfig.props(Props(classOf[DownloaderActor], config.downloader)), "downloader-router")
  private val pageProcessorRouter = context.actorOf(FromConfig.props(Props[PageProcessorActor]), "page-processor-router")
  private val itemProcessorRouter = context.actorOf(FromConfig.props(Props[ItemProcessorActor]), "item-processor-router")

  override def receive: Receive = {
    case CrawlRequestEvent(request) =>
      val preProcessedRequest = requestMiddleware.processRequest(request)
      if(!preProcessedRequest.skip) {
        downloaderRouter ! CrawlRequestEvent(preProcessedRequest)
      } else {
        log.debug(s"Skipped ${request.url}")
      }

    case CrawlResponseEvent(response) =>
      val page = Page(response.rawText, response.url)
      pageProcessorRouter ! HandlePageEvent(page)

    case HandleItemEvent(item) =>
      itemProcessorRouter ! HandleItemEvent(item)

    case msg: UnhandledMessage =>
      log.error("Found unhandled message: " + msg)
  }
}

object Engine {
  val ACTOR_NAME = "engine"

  def props(config: ScrawlerConfig, requestMiddleware: RequestMiddleware): Props =
    Props(new Engine(config, requestMiddleware))
}
