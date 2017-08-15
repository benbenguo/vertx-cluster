package org.dean.verticle;

import io.netty.handler.codec.http.QueryStringDecoder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import org.dean.service.HttpService;

import java.util.List;
import java.util.Map;

/**
 * Created by Dean on 2017/8/8.
 */
public class HttpVerticle extends AbstractVerticle {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HttpVerticle());
    }

    @Override
    public void start() throws Exception {
        super.start();

        HttpServer server = vertx.createHttpServer();

        server.requestHandler(req -> {
            if (req.method() == HttpMethod.GET || req.method() == HttpMethod.POST) {
                req.response().setChunked(true);

                HttpService service = new HttpService();
                service.requestJob(vertx, req);
            } else {
                req.response().setStatusCode(405).end();
            }
        });

        server.listen(8888);
    }
}
