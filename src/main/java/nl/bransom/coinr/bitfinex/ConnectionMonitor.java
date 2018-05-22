package nl.bransom.coinr.bitfinex;

import io.vertx.core.Future;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServerRequest;

public class ConnectionMonitor extends AbstractVerticle {

  @Override
  public void start(final Future<Void> futureResult) {
    vertx
        .createHttpServer()
        .requestHandler(this::respond)
        .listen(8080, result -> {
          if (result.succeeded()) {
            futureResult.complete();
          } else {
            futureResult.fail(result.cause());
          }
        });
  }

  private void respond(final HttpServerRequest request) {
    request
        .response()
        .end("<h1>Hello from the ConnectionMonitor</h1>");
  }
}
