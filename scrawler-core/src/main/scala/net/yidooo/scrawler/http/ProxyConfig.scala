package net.yidooo.scrawler.http

import scala.util.Random

case class HttpProxy(host: String, port: Int) {
  def this(hostPort: String) = this(hostPort.split(':').head, hostPort.split(':').last.toInt)
}

case class ProxyConfig(proxies: Seq[HttpProxy], proxyUser: String, proxyPassword: String) {
  def pickUpOne(): HttpProxy = {
    val rand = new Random(System.currentTimeMillis())
    val randomIndex = rand.nextInt(proxies.length)
    proxies(randomIndex)
  }
}
