package net.yidooo.scrawler.http.impl

import java.nio.charset.CodingErrorAction

import net.yidooo.scrawler.HttpClientConfig
import net.yidooo.scrawler.http._
import org.apache.http.HttpHost
import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.impl.client._
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.impl.cookie.BasicClientCookie

import scala.io.{Codec, Source}

class ApacheHttpClient(config: HttpClientConfig) extends HttpClient {

  private val httpClient = buildClient()

  implicit def proxy2HttpHost(proxy: HttpProxy): HttpHost = new HttpHost(proxy.host, proxy.port)

  override def doGet(request: HttpRequest): HttpResponse = {
    def buildContext(): HttpClientContext = {
      val localContext = HttpClientContext.create

      // set cookies
      val cookieStore = new BasicCookieStore()
      (config.defaultCookies ++ request.cookies).foreach(cookie => {
        val clientCookie = new BasicClientCookie(cookie._1, cookie._2)
        clientCookie.setDomain(config.domain)
        cookieStore.addCookie(clientCookie)
      })
      localContext.setCookieStore(cookieStore)

      localContext
    }

    def buildGetRequest(): HttpGet = {
      val requestConfigBuilder = RequestConfig.custom()
      requestConfigBuilder
        .setConnectionRequestTimeout(config.connectionRequestTimeout)
        .setConnectTimeout(config.connectTimeout)
        .setSocketTimeout(config.socketTimeout)
        .setExpectContinueEnabled(false)

      if(config.proxyEnabled()) {
        val proxy = config.proxyConfig.get.pickUpOne()
        requestConfigBuilder.setProxy(proxy)
      }

      val httpGet = new HttpGet(request.url)
      httpGet.setConfig(requestConfigBuilder.build())
      request.headers.foreach(header => {
        httpGet.setHeader(header._1, header._2)
      })

      httpGet
    }

    val localContext = buildContext()
    val httpGetRequest = buildGetRequest()
    val httpResp = httpClient.execute(httpGetRequest, localContext)
    val statusCode = httpResp.getStatusLine.getStatusCode
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
    val content = Source.fromInputStream(httpResp.getEntity.getContent).mkString

    HttpResponse(content, statusCode)
  }

  private def buildClient(): CloseableHttpClient = {
    val httpClientBuilder = HttpClients.custom
    httpClientBuilder.setConnectionManager(new PoolingHttpClientConnectionManager())
    if(config.proxyEnabled()) {
      val credsProvider = new BasicCredentialsProvider()
      credsProvider.setCredentials(AuthScope.ANY,
        new UsernamePasswordCredentials(config.proxyConfig.get.proxyUser, config.proxyConfig.get.proxyPassword))
      httpClientBuilder.setDefaultCredentialsProvider(credsProvider)
    }

    val cookieStore = new BasicCookieStore()
    config.defaultCookies.foreach(cookie => {
      cookieStore.addCookie(new BasicClientCookie(cookie._1, cookie._2))
    })
    httpClientBuilder.setDefaultCookieStore(cookieStore)

    httpClientBuilder.build()
  }
}