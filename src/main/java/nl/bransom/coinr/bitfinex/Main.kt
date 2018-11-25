package nl.bransom.coinr.bitfinex

import io.vertx.core.Vertx
import org.slf4j.LoggerFactory

private val LOG = LoggerFactory.getLogger("nl.bransom.coinr.bitfinex")!!

fun main(args: Array<String>) {

  Vertx.vertx().deployVerticle(ConnectionMonitor::class.java.name) { result ->
    if (result.succeeded()) {
      LOG.info("We have hyperdrive, captain.")
    } else {
      LOG.error("Error", result.cause())
    }
  }
}
