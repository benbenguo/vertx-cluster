package org.dean.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.dean.service.HttpService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WebVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new WebVerticle());
    }

    @Override
    public void start() throws Exception {
        super.start();

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        // 增加cookies处理器
        router.route().handler(CookieHandler.create());
        // 增加session处理器
        router.route().handler(
                SessionHandler.create(LocalSessionStore.create(vertx)));

//        router.route().handler(BodyHandler.create());

        router.route().handler(BodyHandler.create().setUploadsDirectory("uploads"));

        router.route().handler(routingContext -> {
            HttpServerRequest req = routingContext.request();

            if (req.method() == HttpMethod.GET || req.method() == HttpMethod.POST) {
                req.response().setChunked(true);

                // 获取session
                Session session = routingContext.session();
                Integer count = session.get("count");

                if (count == null) {
                    session.put("count", 0);
                } else {
                    session.put("count", ++count);
                }

                System.out.println("============= " + session.get("count") + " =============");
                String contentType = req.getHeader("Content-Type");

                Map map = new HashMap();
                if (contentType.startsWith("application/json")) {
                    JsonObject jsonObject = routingContext.getBodyAsJson();
                    map = jsonObject.getMap();
                }

                Set files = routingContext.fileUploads();
                MultiMap multiMap = req.params();
                Buffer buffer = routingContext.getBody();
                HttpService service = new HttpService();
                service.requestJob(vertx, req);
            } else {
                req.response().setStatusCode(405).end();
            }
        });

        server.requestHandler(router::accept).listen(8888);
    }
}