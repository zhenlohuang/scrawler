package net.yidooo.scrawler.processor

import net.yidooo.scrawler.event.{CrawlRequestEvent, HandleItemEvent}
import net.yidooo.scrawler.model.{Item, Page, CrawlRequest}
import net.yidooo.scrawler.{Engine, Spider}


trait PageProcessor {
  private lazy val engine = Spider.system.actorSelection("user" + "/" + Engine.ACTOR_NAME)

  def process(page: Page)

  def addRequest(request: CrawlRequest): Unit = {
    engine ! CrawlRequestEvent(request)
  }

  def addItem(item: Item): Unit = {
    engine ! HandleItemEvent(item)
  }
}
