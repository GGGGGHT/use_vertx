package com.ggggght;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import java.lang.invoke.SerializedLambda;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class ResponseTest {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    Router router = Router.router(vertx);

    // router
    //     .get("/some/path")
    //     // this handler will ensure that the response is serialized to json
    //     // the content type is set to "application/json"
    //     .respond(
    //         ctx -> Future.succeededFuture(new JsonObject().put("hello", "world")));
    //
    // router
    //     .get("/some/path")
    //     // this handler will ensure that the Object is serialized to json
    //     // the content type is set to "application/json"
    //     .respond(
    //         ctx -> Future.succeededFuture(new Object()));
    // router
    //     .get("/some/path")
    //     // in this case, the handler ensures that the connection is ended
    //     .respond(
    //         ctx -> ctx
    //             .response()
    //             .setChunked(true)
    //             .write("Write some text..."));
    //
    //
    // router
    //     .get("/some/path")
    //     .respond(
    //         ctx -> ctx
    //             .response()
    //             .putHeader("Content-Type", "text/plain")
    //             .end("hello world!"));

    // Using blocking handlers
    // router.route().blockingHandler(ctx -> {
    //   try {
    //     Thread.sleep(1000);
    //   } catch (InterruptedException e) {
    //     e.printStackTrace();
    //   }
    //
    //   ctx.next();
    // });

    router.post("/some/endpoint").handler(ctx -> {
      ctx.request().setExpectMultipart(true);
      ctx.next();
    }).blockingHandler(ctx -> {
      try {
        TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(
          "Current thread: " + Thread.currentThread().getName() + " , now: " + LocalDateTime.now());
      ctx.next();
    });

    router.post("/some/endpoint").handler(ctx -> {
      ctx.response().write("hello world");
      ctx.response().end();
    });

    vertx.createHttpServer().requestHandler(router)
        .listen(8888)
        .onSuccess(server -> System.out.println("start success" + server.actualPort()));
  }
}
