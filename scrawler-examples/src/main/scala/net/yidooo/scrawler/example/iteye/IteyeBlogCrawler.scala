package net.yidooo.scrawler.example.iteye

import net.yidooo.scrawler.Spider
import net.yidooo.scrawler.middleware.DedupMiddleware
import net.yidooo.scrawler.model._


object IteyeBlogCrawler extends App {
  override def main(args: Array[String]): Unit = {
    val spider = Spider.builder()
      .modules(new IteyeModule())
      .requestMiddleware(new DedupMiddleware())
      .initRequest(new CrawlRequest("http://www.iteye.com/blogs"))
      .build()

    spider.run()
  }
}
