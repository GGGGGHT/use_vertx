package com.ggggght;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class RouterBack {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    Router router = Router.router(vertx);

    router.get("/some/path").handler(ctx -> {

      ctx.put("foo", "bar");
      System.out.println("got A");
      ctx.next();

    });

    router
        .get("/some/path/B")
        .handler(ctx -> {
          System.out.println("come here");
          ctx.response().end(ctx.get("foo").toString());
        });

    router
        .get("/some/path")
        .handler(ctx -> {
          System.out.println("forward to /some/path/B");
          // 可以使用reroute去重定向到另一个router中
          ctx.reroute("/some/path/B");
        });

    vertx.createHttpServer().requestHandler(router)
        .listen(8888)
        .onSuccess(server -> System.out.println("server start at " + server.actualPort()));
  }
}
