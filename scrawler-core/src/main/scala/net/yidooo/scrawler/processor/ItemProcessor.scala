package net.yidooo.scrawler.processor

import net.yidooo.scrawler.model.Item

trait ItemProcessor {
  def process(item: Item)
}
