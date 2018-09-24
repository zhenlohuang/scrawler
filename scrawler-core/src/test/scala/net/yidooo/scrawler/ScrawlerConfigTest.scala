package net.yidooo.scrawler

import org.scalatest.{FlatSpec, Matchers}

class ScrawlerConfigTest extends FlatSpec with Matchers {
  s"${ScrawlerConfig.getClass.getSimpleName}" should "read downloader configuration" in {
    val configString =
      """
        |scrawler {
        |  downloader {
        |    sleepTime = 5000
        |    retryTime = 5
        |    connectionTimeout = 1000
        |    readTimeout = 10000
        |    connectionTimeout = 8000
        |    readTimeout = 40000
        |    proxies = ["127.0.0.1:1087"]
        |    proxyUser = "testUser"
        |    proxyPassword = "testPassword"
        |  }
        |}
      """.stripMargin

    val config = ScrawlerConfig.loadFromString(configString)

    assert(config.downloader.proxyPool == List("127.0.0.1:1087"))
    assert(config.downloader.sleepTime == 5000)
    assert(config.downloader.retryTime == 5)
    assert(config.downloader.connectionTimeout == 8000)
    assert(config.downloader.readTimeout == 40000)

    assert(config.downloader.enableProxy())
    assert(config.downloader.hasProxyAuth())
    assert(config.downloader.proxyUser == "testUser")
    assert(config.downloader.proxyPassword == "testPassword")
  }
}
