package nl.bransom.coinr

import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.AbstractVerticle
import nl.bransom.coinr.bitfinex.BitfinexConnection
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

fun main() = Main.run()

object Main {

    private val log = LoggerFactory.getLogger(javaClass)
    private val verticlesToDeploy = listOf(
        HttpRedirectVerticle::class,
        HttpsServerVerticle::class
    )
    val verticleDeploymentStates = verticlesToDeploy
        .map { it to "not started" }
        .toMap()
        .toMutableMap()

    fun run() {
        val options = VertxOptions()
        if (log.isDebugEnabled) {
            // Allow blocking threads for max 10 minutes (for debugging).
            options.blockedThreadCheckInterval = 1_000 * 60 * 10
        }
        val vertx = Vertx.vertx(options)

        ConfigRetriever
            .create(
                vertx, ConfigRetrieverOptions()
                    .addStore(
                        ConfigStoreOptions()
                            .setType("env")
                            .setConfig(JsonObject())
                    )
            )
            .getConfig { config ->
                val deploymentOptions = DeploymentOptions()
                    .setConfig(config.result())
                    .setWorker(true)
                verticlesToDeploy
                    .forEach {
                        deployVerticle(vertx, it, deploymentOptions)
                    }
            }
    }

    private fun deployVerticle(
        vertx: Vertx,
        verticleClass: KClass<out AbstractVerticle>,
        deploymentOptions: DeploymentOptions
    ) =
        vertx.deployVerticle(verticleClass.java.name, deploymentOptions) { deploymentResult ->
            if (deploymentResult.succeeded()) {
                verticleDeploymentStates[verticleClass] = "UP"
            } else {
                verticleDeploymentStates[verticleClass] = deploymentResult.cause().message ?: "UNKNOWN ERROR"
                log.error("Error deploying ${verticleClass.simpleName}", deploymentResult.cause())
            }
        }
}