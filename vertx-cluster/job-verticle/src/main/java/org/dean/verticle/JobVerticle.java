package org.dean.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import org.dean.vo.Request;

/**
 * Created by Dean on 2017/8/8.
 */
public class JobVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        super.start();
        EventBus eb = vertx.eventBus();

        eb.consumer("test", message -> {
        }).handler(jobHandler());
    }

    private Handler<Message<Object>> jobHandler() {
        return msg -> vertx.executeBlocking(
                future -> {
                    try {
                        Request request = (Request)msg.body();
                        System.out.println("working...");
                        future.complete("received");
                    } catch (Exception e) {
                        future.fail(e);
                    }
                }, result -> {
                    if (result.succeeded()) {
                        msg.reply(result.result());
                    } else {
                        if (result.cause() != null) {
                            msg.reply(result.cause().getMessage());
                        }
                    }
                });
    }
}
