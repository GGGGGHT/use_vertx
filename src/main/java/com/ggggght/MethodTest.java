package com.ggggght;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;

public class MethodTest {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    Router router = Router.router(vertx);
    // 支持多种方法
    router.route("/foo*").method(HttpMethod.PUT).method(HttpMethod.GET).handler(ctx -> {
      HttpMethod method = ctx.request().method();
      System.out.println(method.name());
      System.out.println(ctx.request().absoluteURI());
      ctx.response().end("hello");
    });

    vertx.createHttpServer().requestHandler(router).listen(8888).onSuccess(service -> {
      System.out.println("start success at " + service.actualPort());
    });
  }
}
