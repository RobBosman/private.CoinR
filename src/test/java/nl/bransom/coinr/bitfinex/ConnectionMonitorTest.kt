package nl.bransom.coinr.bitfinex

import io.vertx.core.Vertx
import io.vertx.ext.unit.TestSuite
import org.junit.jupiter.api.Test

class ConnectionMonitorTest {

  @Test
  fun test() {
    val vertx = Vertx.vertx()
    TestSuite
        .create("TestSuite")
        .before { testContext ->
          vertx
              .exceptionHandler(testContext.exceptionHandler())
              .deployVerticle(ConnectionMonitor::class.java.name, testContext.asyncAssertSuccess<String>())
        }
        .test("pingTest") { testContext ->
          val async = testContext.async()
          vertx
              .exceptionHandler(testContext.exceptionHandler())
              .createHttpClient()
              .getNow(8080, "localhost", "/") { response ->
                response.handler { body ->
                  testContext.assertTrue(body.toString().contains("Hello from the ConnectionMonitor"))
                  async.complete()
                }
              }
        }
        .after { testContext -> vertx.close(testContext.asyncAssertSuccess()) }
        .run()
        .awaitSuccess()
  }
}
