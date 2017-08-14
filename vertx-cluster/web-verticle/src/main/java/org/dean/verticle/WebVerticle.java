package org.dean.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import org.dean.service.HttpService;

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
        // 增加cookies处理器，解码cookies，并将其放到context上下文中
        router.route().handler(CookieHandler.create());
        // 增加session处理器，为每次用户请求，维护一个唯一的session，这里使用内存session，后面会讲分布式的session存储
        router.route().handler(
                SessionHandler.create(LocalSessionStore.create(vertx)));

        router.route().handler(routingContext -> {
            HttpServerRequest req = routingContext.request();

            if (req.method() == HttpMethod.GET || req.method() == HttpMethod.POST) {
                req.response().setChunked(true);

                // 从请求上下文获取session
                Session session = routingContext.session();
                Integer count = session.get("count");

                if (count == null) {
                    session.put("count", 0);
                } else {
                    session.put("count", ++count);
                }

                System.out.println("============= " + session.get("count") + " =============");

                HttpService service = new HttpService();
                service.requestJob(vertx, req);
            } else {
                req.response().setStatusCode(405).end();
            }
        });

        server.requestHandler(router::accept).listen(8888);
    }
}