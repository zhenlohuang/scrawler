package net.yidooo.scrawler.middleware

import java.util.Collections

import scala.collection.JavaConverters._
import java.util.concurrent.ConcurrentHashMap

import net.yidooo.scrawler.model.CrawlRequest

class DedupMiddleware extends RequestMiddleware {
  private lazy val requestSet = createSet[String]()

  override def processRequest(request: CrawlRequest): CrawlRequest = {
    if(!requestSet.contains(request.url)) {
      requestSet.add(request.url)
      request
    } else {
      request.copy(skip = true)
    }
  }

  def createSet[T]() = {
    Collections.newSetFromMap(new ConcurrentHashMap[T, java.lang.Boolean]).asScala
  }
}
