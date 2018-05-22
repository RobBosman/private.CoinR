package nl.bransom.coinr.bitfinex;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ConnectionMonitorTest {

  private Vertx vertx;

  @Before
  public void setUp(final TestContext context) {
    vertx = Vertx.vertx();
    vertx.deployVerticle(ConnectionMonitor.class.getName(), context.asyncAssertSuccess());
  }

  @After
  public void tearDown(final TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testMyApplication(final TestContext context) {
    final Async async = context.async();
    vertx.createHttpClient().
        getNow(8080, "localhost", "/",
            response -> response.handler(body -> {
              context.assertTrue(body.toString().contains("Hello from the ConnectionMonitor"));
              async.complete();
            }));
  }
}
