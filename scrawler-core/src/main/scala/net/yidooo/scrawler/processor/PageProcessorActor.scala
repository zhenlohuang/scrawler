package net.yidooo.scrawler.processor

import akka.actor.{Actor, ActorLogging, Props, UnhandledMessage}
import net.yidooo.scrawler.Spider
import net.yidooo.scrawler.event.HandlePageEvent

class PageProcessorActor extends Actor with ActorLogging {
  lazy val processor: PageProcessor = Spider.module.pageProcessor

  override def receive: Receive = {
    case HandlePageEvent(page) =>
      log.debug(s"Processing url: ${page.url}")
      processor.process(page)
    case msg: UnhandledMessage =>
      log.error("Found unhandled message: " + msg)
  }
}

object PageProcessorActor {
  def props(): Props = Props(new PageProcessorActor())
}

