package com.ggggght;

import io.vertx.core.Vertx;
import io.vertx.ext.web.LanguageHeader;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class I18nTest {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    Router router = Router.router(vertx);
    router.get("/localized").handler(ctx -> {
      // although it might seem strange by running a loop with a switch we
      // make sure that the locale order of preference is preserved when
      // replying in the users language.
      ctx.response().putHeader("Content-type", "text/plain");
      for (LanguageHeader language : ctx.acceptableLanguages()) {
        switch (language.tag()) {
          case "en" -> ctx.response().end("Hello!");
          case "fr" -> ctx.response().end("Bonjour!");
          case "pt" -> ctx.response().end("Olá!");
          case "es" -> ctx.response().end("Hola!");
          case "cn" -> ctx.response().end("你好");
          default -> ctx.response().end("error!");
        }
      }
      // we do not know the user language so lets just inform that back:
      ctx.response().end("Sorry we don't speak: " + ctx.preferredLanguage());
    });

    vertx.createHttpServer().requestHandler(router).listen(8888).onSuccess(service -> {
      System.out.println("start success at " + service.actualPort());
    });
  }
}