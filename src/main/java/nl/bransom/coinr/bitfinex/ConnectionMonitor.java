package nl.bransom.coinr.bitfinex;

import io.vertx.core.Future;
import io.vertx.rxjava.core.AbstractVerticle;

public class ConnectionMonitor extends AbstractVerticle {

  @Override
  public void start(final Future<Void> fut) {
    vertx
        .createHttpServer()
        .requestHandler(r -> r.response().end("<h1>Hello from the ConnectionMonitor</h1>"))
        .listen(8080, result -> {
          if (result.succeeded()) {
            fut.complete();
          } else {
            fut.fail(result.cause());
          }
        });
  }
}
