package com.ggggght;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class SubRouters {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    Router mainRouter = Router.router(vertx);
    mainRouter.route("/static/*").handler(myStaticHandler -> {
      System.out.println("static resources...");
    });
    mainRouter.routeWithRegex(".*\\.templ").handler(myTemplateHandler -> {
      System.out.println("template ...");
    });

    Router restAPI = Router.router(vertx);

    restAPI.get("/products/:test").handler(ctx -> {

      // TODO Handle the lookup of the product....
      String productId = ctx.pathParam("test");
      ctx.response().end("get input  productId is: " + productId + "\n");

    });

    restAPI.put("/products/:productId").handler(ctx -> {

      // TODO Add a new product...
      String productId = ctx.pathParam("productId");
      ctx.response().end("put input  productId is: " + productId + "\n");
    });

    restAPI.delete("/products/:productId").handler(ctx -> {

      // TODO delete the product...
      String productId = ctx.pathParam("productId");
      ctx.response().end("delete input  productId is: " + productId + "\n");

    });

    mainRouter.mountSubRouter("/productsAPI", restAPI);
    vertx.createHttpServer()
        // Handle every request using the router
        .requestHandler(restAPI)
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
