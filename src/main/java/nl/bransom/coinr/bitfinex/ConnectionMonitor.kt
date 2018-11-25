package nl.bransom.coinr.bitfinex

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.Router
import org.slf4j.LoggerFactory

class ConnectionMonitor : AbstractVerticle() {

  private val LOG = LoggerFactory.getLogger(javaClass)!!
  private val PORT = 8080

  override fun start(result: Future<Void>) {
    val router = Router.router(vertx)

    router.get("/").handler { routingContext ->
      routingContext
          .response()
          .end("<h1>Hello from " + Thread.currentThread().name + "</h1>")
    }

    vertx
        .createHttpServer()
        .requestHandler(this::respond)
        .listen(8080) { listenResult ->
          if (listenResult.succeeded()) {
            LOG.info("Listening on http://localhost:{}/", PORT)
            result.complete()
          } else {
            result.fail(listenResult.cause())
          }
        }
  }

  private fun respond(request: HttpServerRequest) {
    request
        .response()
        .end("<h1>Hello from the ConnectionMonitor</h1>")
  }
}