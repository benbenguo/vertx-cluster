package org.dean.server;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.dean.verticle.JobVerticle;
import org.dean.vo.Request;
import org.dean.vo.RequestMsgCodec;

import java.io.InputStream;

public class JobStart {

    private static Vertx vertx;

    public static void main(String[] args) throws Exception {
//        Config hazelcastConfig = new Config();
//        hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1").setEnabled(true);
//        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        InputStream in = JobStart.class.getClassLoader().getResourceAsStream("cluster.xml");
        Config config = new XmlConfigBuilder(in).build();

        ClusterManager mgr = new HazelcastClusterManager(config);
        VertxOptions options = new VertxOptions().setClusterManager(mgr);

        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                vertx = res.result();
                vertx.eventBus().registerDefaultCodec(Request.class, new RequestMsgCodec());
                vertx.deployVerticle(new JobVerticle());
            } else {
                if (res.cause() != null) {
                    res.cause().printStackTrace();
                }
            }
        });
    }
}
