package nl.bransom.coinr.bitfinex;

import io.vertx.core.Vertx;

public class Main {

  public static void main(final String[] args) {
    Vertx.vertx().deployVerticle(ConnectionMonitor.class.getName());
  }
}
