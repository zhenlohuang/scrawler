package net.yidooo.scrawler.processor

import akka.actor.{Actor, ActorLogging, Props, UnhandledMessage}
import net.yidooo.scrawler.Spider
import net.yidooo.scrawler.event.HandleItemEvent

class ItemProcessorActor() extends Actor with ActorLogging {
  lazy val processor: ItemProcessor = Spider.module.itemProcessor

  override def receive: Receive = {
    case HandleItemEvent(item) =>
      log.debug(s"Processing item: $item")
      processor.process(item)
    case msg: UnhandledMessage =>
      log.error("Found unhandled message: " + msg)
  }
}

object ItemProcessorActor {
  def props(): Props = Props(new ItemProcessorActor())
}
