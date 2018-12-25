package net.yidooo.scrawler

import net.yidooo.scrawler.http.HttpProxy
import org.scalatest.{FlatSpec, Matchers}

class ScrawlerConfigTest extends FlatSpec with Matchers {
  s"${ScrawlerConfig.getClass.getSimpleName}" can "read downloader configuration" in {
    val configString =
      """
        |scrawler {
        |  downloader {
        |    sleepTime = 5000
        |    retryTime = 5
        |    http {
        |        connectionRequestTimeout = 5000
        |        connectionTimeout = 5000
        |        socketTimeout = 5000
        |        proxies = ["127.0.0.1:8087"]
        |        proxyUser = "testUser"
        |        proxyPassword = "testPwd"
        |        cookies = ["language:cn_CN"]
        |        domain = "testDomain"
        |      }
        |  }
        |}
      """.stripMargin

    val config = ScrawlerConfig.loadFromString(configString)

    assert(config.downloader.sleepTime == 5000)
    assert(config.downloader.retryTime == 5)

    assert(config.downloader.httpClientConfig.connectionRequestTimeout == 5000)
    assert(config.downloader.httpClientConfig.connectTimeout == 5000)
    assert(config.downloader.httpClientConfig.socketTimeout == 5000)
    assert(config.downloader.httpClientConfig.proxyConfig.isDefined)
    assert(config.downloader.httpClientConfig.proxyConfig.get.proxies == Seq(HttpProxy("127.0.0.1", 8087)))
    assert(config.downloader.httpClientConfig.proxyConfig.get.proxyUser == "testUser")
    assert(config.downloader.httpClientConfig.proxyConfig.get.proxyPassword == "testPwd")
    assert(config.downloader.httpClientConfig.defaultCookies == Map("language" -> "cn_CN"))
    assert(config.downloader.httpClientConfig.domain == "testDomain")
  }
}
