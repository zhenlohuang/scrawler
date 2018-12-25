package net.yidooo.scrawler.example.iteye

import net.yidooo.scrawler.model.{Item, Page, CrawlRequest}
import net.yidooo.scrawler.processor.PageProcessor

class IteyePageProcessor extends PageProcessor {
  private val DOMAIN = "http://www.iteye.com"
  private val BLOG_URL_PATTERN = "http://[\\w-]+.iteye.com/blog/\\d+".r
  private val PAGE_URL_PATTERN = "/blogs?page=\\d+".r

  override def process(page: Page): Unit = {
    page.url match {
      case BLOG_URL_PATTERN(_*) =>
        val blogTitle = page.html.body.cssSelect("#main > div.blog_main > div.blog_title > h3 > a").map(_.text).headOption
        if(blogTitle.isDefined) {
          addItem(Item(Map("title" -> blogTitle.get, "url" -> page.url)))
        }
      case _ =>
        page.html.body.links.foreach {
          case link@(BLOG_URL_PATTERN(_*) | PAGE_URL_PATTERN(_*)) =>
            if(link.startsWith("http")) {
              addRequest(CrawlRequest.of(link))
            } else {
              addRequest(CrawlRequest.of(DOMAIN + link))
            }
          case _ =>
        }
    }
  }
}
