package kamon

import java.time.Duration

import com.typesafe.config.Config
import org.slf4j.LoggerFactory
import java.net.Proxy

package object kamino {
  private val logger = LoggerFactory.getLogger("kamon.kamino")

  def readConfiguration(config: Config): KaminoConfiguration = {
    val kaminoConfig = config.getConfig("kamino")
    val apiKey = kaminoConfig.getString("api-key")

    if(apiKey.equals("none"))
      logger.error("No API key defined in the kamino.api-key setting.")

    KaminoConfiguration(
      apiKey            = apiKey,
      connectionTimeout = kaminoConfig.getDuration("client.timeouts.connection"),
      readTimeout       = kaminoConfig.getDuration("client.timeouts.read"),
      appVersion        = kaminoConfig.getString("app-version"),
      ingestionApi      = kaminoConfig.getString("ingestion-api"),
      bootRetries       = kaminoConfig.getInt("retries.boot"),
      ingestionRetries  = kaminoConfig.getInt("retries.ingestion"),
      shutdownRetries   = kaminoConfig.getInt("retries.shutdown"),
      tracingRetries    = kaminoConfig.getInt("retries.tracing"),
      retryBackoff      = kaminoConfig.getDuration("retries.backoff"),
      clientBackoff     = kaminoConfig.getDuration("client.backoff"),
      proxyHost         = kaminoConfig.getString("proxy.host"),
      proxyPort         = kaminoConfig.getInt("proxy.port"),
      proxy             = kaminoConfig.getString("proxy.type").toLowerCase match {
        case "system" => None
        case "socks"  => Some(Proxy.Type.SOCKS)
        case "https"   => Some(Proxy.Type.HTTP)
      }
    )
  }

  case class KaminoConfiguration(
    apiKey: String,
    connectionTimeout: Duration,
    readTimeout: Duration,
    appVersion: String,
    ingestionApi: String,
    bootRetries: Int,
    ingestionRetries: Int,
    shutdownRetries: Int,
    tracingRetries: Int,
    retryBackoff: Duration,
    clientBackoff: Duration,
    proxy: Option[Proxy.Type],
    proxyHost: String,
    proxyPort: Int
  ) {
    def ingestionRoute  = s"$ingestionApi/ingest"
    def bootMark        = s"$ingestionApi/hello"
    def shutdownMark    = s"$ingestionApi/goodbye"
    def tracingRoute    = s"$ingestionApi/tracing/ingest"
  }

}
