package net.yidooo.scrawler.example.iteye

import com.softwaremill.macwire._
import net.yidooo.scrawler.ScrawlerModule
import net.yidooo.scrawler.processor.{ConsoleProcessor, ItemProcessor, PageProcessor}

class IteyeModule extends ScrawlerModule {
  override def pageProcessor: PageProcessor = wire[IteyePageProcessor]

  override def itemProcessor: ItemProcessor = wire[ConsoleProcessor]
}
