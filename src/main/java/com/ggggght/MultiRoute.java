package com.ggggght;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import java.time.LocalDateTime;

public class MultiRoute {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    Router router = Router.router(vertx);
    Route route = router.route("/some/path/");
    route.handler(ctx -> {

      HttpServerResponse response = ctx.response();
      // enable chunked responses because we will be adding data as
      // we execute over other handlers. This is only required once and
      // only if several handlers do output.
      response.setChunked(true);

      response.write("route1\n");
      System.out.println("handler 1" + LocalDateTime.now());

      // Call the next matching route after a 5 second delay
      ctx.vertx().setTimer(5000, tid -> ctx.next());
    });

    route.handler(ctx -> {

      HttpServerResponse response = ctx.response();
      response.write("route2\n");
      System.out.println("handler 2" + LocalDateTime.now());

      // Call the next matching route after a 5 second delay
      ctx.vertx().setTimer(5000, tid -> ctx.next());
    });

    route.handler(ctx -> {

      HttpServerResponse response = ctx.response();
      response.write("route3");
      System.out.println("handler 3" + LocalDateTime.now());

      // Now end the response
      ctx.response().end();
    });

    vertx.createHttpServer()
        // Handle every request using the router
        .requestHandler(router)
        // Start listening
        .listen(8888)
        // Print the port
        .onSuccess(server ->
            System.out.println(
                "HTTP server started on port " + server.actualPort()
            )
        );
  }
}
