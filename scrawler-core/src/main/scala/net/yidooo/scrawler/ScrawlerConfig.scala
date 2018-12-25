package net.yidooo.scrawler

import com.typesafe.config.{Config, ConfigFactory}
import net.yidooo.scrawler.http.{HttpProxy, ProxyConfig}

import scala.collection.JavaConverters._

class ScrawlerConfig(config: Config) {
  lazy val downloader = new DownloaderConfig(config.getConfig("scrawler.downloader"))
}

object ScrawlerConfig {
  lazy val DEFAULT_USER_CONFIG_FILE = "scrawler.conf"
  private lazy val DEFAULT_CONFIG_FILE = "scrawler-default.conf"

  private lazy val defaultConfig = ConfigFactory.load(DEFAULT_CONFIG_FILE)

  def load(filename: String = DEFAULT_USER_CONFIG_FILE): ScrawlerConfig = {
    new ScrawlerConfig(ConfigFactory.load(filename).withFallback(defaultConfig))
  }

  def loadFromString(configString: String): ScrawlerConfig = {
    new ScrawlerConfig(ConfigFactory.parseString(configString).withFallback(defaultConfig))
  }
}

private[scrawler] class DownloaderConfig(config: Config) {
  private val SLEEP_TIME_PROP = "sleepTime"
  private val RETRY_TIME_PROP = "retryTime"
  lazy val sleepTime: Int = config.getInt(SLEEP_TIME_PROP)
  lazy val retryTime: Int = config.getInt(RETRY_TIME_PROP)
  lazy val httpClientConfig: HttpClientConfig = new HttpClientConfig(config.getConfig("http"))
}

private[scrawler] class HttpClientConfig(config: Config) {
  private val CONNECTION_REQUEST_TIMEOUT_PROP = "connectionRequestTimeout"
  private val CONNECTION_TIMEOUT_PROP = "connectionTimeout"
  private val SOCKET_TIMEOUT_PROP = "socketTimeout"
  private val HTTP_PROXY_PROP = "proxies"
  private val HTTP_PROXY_USER_PROP = "proxyUser"
  private val HTTP_PROXY_PASSWORD_PROP = "proxyPassword"
  private val COOKIES_PROP = "cookies"
  private val DOMAIN_PROP = "domain"

  lazy val connectionRequestTimeout = config.getInt(CONNECTION_REQUEST_TIMEOUT_PROP)
  lazy val connectTimeout = config.getInt(CONNECTION_TIMEOUT_PROP)
  lazy val socketTimeout = config.getInt(SOCKET_TIMEOUT_PROP)
  lazy val proxyConfig: Option[ProxyConfig] = readProxyConfig()
  lazy val defaultCookies: Map[String, String] = readDefaultCookies()
  lazy val domain: String = config.getString(DOMAIN_PROP)

  private def readProxyConfig(): Option[ProxyConfig] = {
    if(config.getStringList(HTTP_PROXY_PROP).isEmpty) {
      None
    } else {
      val proxyPool: Seq[HttpProxy] = config.getStringList(HTTP_PROXY_PROP).asScala
        .map(hostPort => HttpProxy(hostPort.split(':').head, hostPort.split(':').last.toInt))
      val proxyUser = config.getString(HTTP_PROXY_USER_PROP)
      val proxyPassword: String = config.getString(HTTP_PROXY_PASSWORD_PROP)
      val proxyConfig = ProxyConfig(proxyPool, proxyUser, proxyPassword)
      Some(proxyConfig)
    }
  }

  private def readDefaultCookies(): Map[String, String] = {
    config.getStringList(COOKIES_PROP).asScala
      .map(cookieString => (cookieString.split(':').head, cookieString.split(':').last))
      .toMap
  }

  def proxyEnabled(): Boolean = {
    proxyConfig.isDefined
  }
}




