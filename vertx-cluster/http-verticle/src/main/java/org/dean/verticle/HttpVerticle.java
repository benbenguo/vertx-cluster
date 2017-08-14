package org.dean.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import org.dean.service.HttpService;

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

                HttpService service = new HttpService();
                service.requestJob(vertx, req);
            } else {
                req.response().setStatusCode(405).end();
            }
        });

        server.listen(8888);
    }
}
