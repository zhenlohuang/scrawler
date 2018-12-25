package net.yidooo.scrawler.downloader

import net.yidooo.scrawler.model.CrawlResponse
import net.yidooo.scrawler.model.CrawlRequest

trait Downloader {
  def download(request: CrawlRequest): CrawlResponse
}
