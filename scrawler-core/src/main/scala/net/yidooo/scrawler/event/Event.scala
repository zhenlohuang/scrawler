package net.yidooo.scrawler.event

import net.yidooo.scrawler.model.{Item, Page, Request, Response}

final case class CrawlRequestEvent(request: Request)
final case class CrawlResponseEvent(response: Response)
final case class HandleItemEvent(item: Item)
final case class FetchTaskEvent()
final case class HandlePageEvent(page: Page)
