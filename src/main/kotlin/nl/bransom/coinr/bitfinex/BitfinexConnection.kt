package nl.bransom.coinr.bitfinex

import io.vertx.core.http.HttpMethod.GET
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.client.WebClient

object BitfinexConnection {

    fun ping(vertx: Vertx) =
        WebClient
            .create(vertx)
            .requestAbs(GET, "https://api-pub.bitfinex.com/v2/platform/status")
            .ssl(true)
            .rxSend()
            .map { it.bodyAsJsonArray().getNumber(0) }
            .doOnSuccess { if (it != 1) error("Bitfinex is responding but not available.") }
}