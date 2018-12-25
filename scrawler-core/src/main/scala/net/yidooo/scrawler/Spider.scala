package net.yidooo.scrawler

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import net.yidooo.scrawler.event.CrawlRequestEvent
import net.yidooo.scrawler.middleware.{DummyMiddleware, RequestMiddleware}
import net.yidooo.scrawler.model.CrawlRequest

import scala.collection.mutable.ArrayBuffer

class Spider(config: ScrawlerConfig, requestMiddleware: RequestMiddleware, initRequests: Seq[CrawlRequest])
  extends LazyLogging {
  private val engine = Spider.system.actorOf(Engine.props(config, requestMiddleware), Engine.ACTOR_NAME)

  def run() = {
    initRequests.foreach(engine ! CrawlRequestEvent(_))
  }
}

object Spider {
  private val SYSTEM_NAME = "scrawler"
  val system = ActorSystem(SYSTEM_NAME)
  var module: ScrawlerModule = null //TODO: Refactor required.

  def builder(): Builder = new Builder

  class Builder {
    private var requestMiddleware: RequestMiddleware = new DummyMiddleware()
    private var initRequests = new ArrayBuffer[CrawlRequest]()
    private var configFile = ScrawlerConfig.DEFAULT_USER_CONFIG_FILE

    def modules(module: ScrawlerModule): Builder = {
      Spider.module = module
      this
    }

    def requestMiddleware(requestMiddleware: RequestMiddleware): Builder = {
      this.requestMiddleware = requestMiddleware
      this
    }

    def initRequest(request: CrawlRequest): Builder = {
      this.initRequests += request
      this
    }

    def initRequests(requests: Seq[CrawlRequest]): Builder = {
      this.initRequests ++= requests
      this
    }

    def initUrls(urls: Seq[String]): Builder = {
      this.initRequests ++= urls.map(CrawlRequest.of)
      this
    }

    def initUrl(url: String): Builder = {
      this.initRequests += CrawlRequest.of(url)
      this
    }

    def configFile(filename: String): Builder = {
      this.configFile = filename
      this
    }

    def build(): Spider = {
      require(initRequests.nonEmpty)

      new Spider(ScrawlerConfig.load(configFile), requestMiddleware, initRequests)
    }
  }
}