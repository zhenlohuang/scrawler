package net.yidooo.scrawler

import java.net.Proxy

import com.typesafe.config.{Config, ConfigFactory}
import scalaj.http.HttpConstants

import scala.collection.JavaConverters._
import scala.util.Random

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

  private val HTTP_PROXY_PROP = "proxies"
  private val HTTP_PROXY_USER_PROP = "proxyUser"
  private val HTTP_PROXY_PASSWORD_PROP = "proxyPassword"
  private val SLEEP_TIME_PROP = "sleepTime"
  private val RETRY_TIME_PROP = "retryTime"
  private val CONNECTION_TIMEOUT_PROP = "connectionTimeout"
  private val READ_TIMEOUT_PROP = "readTimeout"

  lazy val proxyPool: List[String] = config.getStringList(HTTP_PROXY_PROP).asScala.toList
  lazy val proxyUser: String = config.getString(HTTP_PROXY_USER_PROP)
  lazy val proxyPassword: String = config.getString(HTTP_PROXY_PASSWORD_PROP)
  lazy val sleepTime: Int = config.getInt(SLEEP_TIME_PROP)
  lazy val retryTime: Int = config.getInt(RETRY_TIME_PROP)
  lazy val connectionTimeout: Int = config.getInt(CONNECTION_TIMEOUT_PROP)
  lazy val readTimeout: Int = config.getInt(READ_TIMEOUT_PROP)

  def enableProxy(): Boolean = {
    proxyPool.nonEmpty
  }

  def hasProxyAuth(): Boolean = {
    proxyUser.nonEmpty && proxyPassword.nonEmpty
  }

  def getProxy(): Proxy = {
    val rand = new Random(System.currentTimeMillis())
    val randomIndex = rand.nextInt(proxyPool.length)
    HttpConstants.proxy(proxyPool(randomIndex).split(":").head, proxyPool(randomIndex).split(":").last.toInt)
  }
}





