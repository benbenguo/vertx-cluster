package org.dean.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;

/**
 * Created by Dean on 2017/8/8.
 */
public class HttpVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        super.start();

        HttpServer server = vertx.createHttpServer();

        server.requestHandler(req -> {
            if (req.method() == HttpMethod.GET || req.method() == HttpMethod.POST) {
                req.response().setChunked(true);

                vertx.eventBus().send("test", "", reply -> {
                    if (reply.succeeded()) {
                        req.response().setStatusCode(200).write(reply.result().body().toString()).end();
                    } else {
                        if(reply.cause() != null) {
                            req.response().setStatusCode(200).write(reply.cause().getMessage()).end();
                        } else {
                            req.response().setStatusCode(200).write("Failed!").end();
                        }
                    }

                });
            } else {
                req.response().setStatusCode(405).end();
            }
        });

        server.listen(8888);
    }
}
