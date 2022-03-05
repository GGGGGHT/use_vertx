package com.ggggght;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

/**
 * 路径匹配
 */
public class PathMatching {
  public static void main(String[] args) {
    for (String arg : args) {
      System.out.println(arg);
    }
    Vertx vertx = Vertx.vertx();
    Router router = Router.router(vertx);
    // routingByExactPath(router);
    // capturingPathParameters(router);
    // capturingGetParameters(router);
    // routingWithRegularExpressions(router);
    capturingPathParametersWithRegularExpressions(router);

    vertx.createHttpServer()
        .requestHandler(router)
        .listen(8080)
        .onSuccess(server -> System.out.println("server start at " + server.actualPort()));
  }

  private static void routingByExactPath(Router router) {
    Route route = router.route().path("/some/path/*");

    route.handler(ctx -> {
      // This handler will be called for any path that starts with
      // `/some/path/`, e.g.

      // `/some/path/`
      // `/some/path/subdir`
      // `/some/path/subdir/blah.html`
      //
      // but **ALSO**:
      // `/some/path` the final slash is always optional with a wildcard to preserve
      //              compatibility with many client libraries.
      // but **NOT**:
      // `/some/patha`
      // `/some/patha/`
      // etc...
      System.out.println("real path is: " + ctx.request().uri());

      ctx.response().end("hello");
    });
  }

  private static void capturingPathParameters(Router router) {
    router
        .route(HttpMethod.POST, "/catalogue/products/:productType/:productID/")
        .handler(ctx -> {

          String productType = ctx.pathParam("productType");
          String productID = ctx.pathParam("productID");

          // Do something with them...
          System.out.println("productType = " + productType);
          System.out.println("productID = " + productID);
        });
  }

  private static void capturingGetParameters(Router router) {
    router
        .route(HttpMethod.GET, "/flights/:from-:to")
        .handler(ctx -> {
          // when handling requests to /flights/AMS-SFO will set:
          String from = ctx.pathParam("from"); // AMS
          String to = ctx.pathParam("to"); // SFO
          // -Dio.vertx.web.route.param.extended-pattern=true
          // remember that this will not work as expected when the parameter
          // naming pattern in use is not the "extended" one. That is because in
          // that case "-" is considered to be part of the variable name and
          // not a separator.
          System.out.println("from = " + from);
          System.out.println("to = " + to);

          ctx.response().end("h");
        });
  }


  private static void routingWithRegularExpressions(Router router) {
    Route route = router.routeWithRegex(".*foo");
    // Route route = router.route().pathRegex(".*foo");

    route.handler(ctx -> {

      // This handler will be called for:

      // /some/path/foo
      // /foo
      // /foo/bar/wibble/foo
      // /bar/foo

      // But not:
      // /bar/wibble
      String path = ctx.request().path();
      System.out.println("request path is: " + path);
      ctx.response().end("path is: " + path);
    });
  }

  private static void capturingPathParametersWithRegularExpressions(Router router) {
    Route route = router.route();

    // This regular expression matches paths that start with something like:
    // "/foo/bar" - where the "foo" is captured into param0 and the "bar" is
    // captured into param1
    route.pathRegex("\\/([^\\/]+)\\/([^\\/]+)").handler(ctx -> {

      String productType = ctx.pathParam("param0");
      String productID = ctx.pathParam("param1");

      // Do something with them...
      System.out.println("productType = " + productType);
      System.out.println("productID = " + productID);

      ctx.response().end("hello");
    });
  }
}
