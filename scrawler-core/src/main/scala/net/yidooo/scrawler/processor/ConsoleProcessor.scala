package net.yidooo.scrawler.processor

import net.yidooo.scrawler.model.Item

class ConsoleProcessor extends ItemProcessor {
  override def process(item: Item): Unit = {
    println(item.fields)
  }
}
