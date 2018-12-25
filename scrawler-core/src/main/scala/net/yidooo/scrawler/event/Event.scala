package net.yidooo.scrawler.event

import net.yidooo.scrawler.model.{Item, Page, CrawlRequest, CrawlResponse}

final case class CrawlRequestEvent(request: CrawlRequest)
final case class CrawlResponseEvent(response: CrawlResponse)
final case class HandleItemEvent(item: Item)
final case class HandlePageEvent(page: Page)
