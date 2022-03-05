package com.ggggght;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import java.util.concurrent.atomic.LongAdder;

public class RouterOrder {
  /**
   * 默认情况下，路由按照添加到路由器的顺序进行匹配。
   *
   * @param args
   */
  public static void main(String[] args) {
    LongAdder adder = new LongAdder();
    Vertx vertx = Vertx.vertx();

    // If you want to override the default ordering for routes, you can do so using <b>order</b>, specifying an integer value.
    Router router = Router.router(vertx);
    // Note: Route order can be specified only before you configure an handler!
    router.route("/some/path/").order(3).handler(ctx -> {

      HttpServerResponse response = ctx.response();

      response.write("route1\n");

      // Now call the next matching route
      response.end();
    });

    router.route("/some/path/").order(1).handler(ctx -> {

      HttpServerResponse response = ctx.response();
      response.write("route2\n");

      // Now call the next matching route
      ctx.next();
    });

    router.route("/some/path/").order(0).handler(ctx -> {

      HttpServerResponse response = ctx.response();
      // enable chunked responses because we will be adding data as
      // we execute over other handlers. This is only required once and
      // only if several handlers do output.
      /// !!! important must be set chunked in first handler
      response.setChunked(true);
      response.write("route3\n");

      // adder.add(1);
      // System.out.println("now comes " + adder);
      // Now end the response
      ctx.next();
    });

    vertx.createHttpServer().requestHandler(router)
        .listen(8888)
        .onSuccess(server -> System.out.println("start success" + server.actualPort()));
  }
}
