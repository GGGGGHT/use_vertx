package com.ggggght;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class MetadataTest {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    Router router = Router.router(vertx);

    router
        .route(HttpMethod.GET, "/metadata/route")
        .putMetadata("metadata-key", "123")
        .handler(ctx -> {
          Route route = ctx.currentRoute();
          String value = route.getMetadata("metadata-key"); // 123
          // will end the request with the value 123
          ctx.end(value);
        });

    vertx.createHttpServer().requestHandler(router)
        .listen(8888)
        .onSuccess(server -> System.out.println("server start at " + server.actualPort()));
  }
}
