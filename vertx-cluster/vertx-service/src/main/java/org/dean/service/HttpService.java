package org.dean.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;

public class HttpService {
    public void requestJob(Vertx vertx, HttpServerRequest request) {
        vertx.eventBus().send("test", "", reply -> {
            if (reply.succeeded()) {
                request.response().setStatusCode(200).write(reply.result().body().toString()).end();
            } else {
                if(reply.cause() != null) {
                    request.response().setStatusCode(200).write(reply.cause().getMessage()).end();
                } else {
                    request.response().setStatusCode(200).write("Failed!").end();
                }
            }
        });
    }
}
