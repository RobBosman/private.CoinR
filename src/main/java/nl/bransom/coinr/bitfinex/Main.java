package nl.bransom.coinr.bitfinex;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(final String[] args) {
    LOG.info("Hello CoinR!");

    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(ConnectionMonitor.class.getName());
  }
}
