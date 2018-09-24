package net.yidooo.scrawler.downloader

import net.yidooo.scrawler.model.Response
import net.yidooo.scrawler.model.Request

trait Downloader {
  def download(request: Request): Response
}
