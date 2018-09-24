package net.yidooo.scrawler

import net.yidooo.scrawler.processor.{ItemProcessor, PageProcessor}

trait ScrawlerModule {
  def pageProcessor: PageProcessor
  def itemProcessor: ItemProcessor
}
