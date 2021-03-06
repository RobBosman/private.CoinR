package nl.bransom.coinr

import io.vertx.core.json.JsonObject
import io.vertx.ext.healthchecks.Status
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.healthchecks.HealthCheckHandler
import nl.bransom.coinr.bitfinex.BitfinexConnection
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

internal object HealthChecker {

    private val log = LoggerFactory.getLogger(javaClass)

    fun getHandler(vertx: Vertx): HealthCheckHandler =
        HealthCheckHandler
            .create(vertx)

            // Check if all verticles are running.
            .register("Verticles", 2_000) { healthStatus ->
                val verticlesNotUp = Main.verticleDeploymentStates
                    .filter { (_, state) -> state != "UP" }
                if (verticlesNotUp.isEmpty()) {
                    healthStatus.complete(Status.OK())
                } else {
                    // Not all verticles are properly started.
                    val details = JsonObject()
                    verticlesNotUp
                        .forEach { (verticleClass, state) ->
                            details.put(verticleClass.simpleName, state)
                        }
                    log.warn("Not all verticles are up-and-running. ${details.encode()}")
                    healthStatus.complete(Status.KO(details))
                }
            }

            // Chek if MongoDB is up and running.
            .register("BitfinexConnection", 2_000) { healthStatus ->
                BitfinexConnection
                    .getStatus(vertx)
                    .timeout(1_000, TimeUnit.MILLISECONDS)
                    .subscribe(
                        {
                            healthStatus.tryComplete(Status.OK())
                        },
                        {
                            log.warn("Bitfinex is not available.", it)
                            healthStatus.tryComplete(Status.KO(JsonObject().put("error", it.message)))
                        })
            }
}