package com.github.mambat.seckill.utils;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.function.Consumer;

/**
 * Verticle Runner.
 *
 * @author wanglei
 * @date 17/2/22 下午12:10
 */
public class Runner {
    private static final String JAVA_DIR = "src/main/java";

    public static void runCluster(Class clazz) {
        run(clazz, new VertxOptions().setClustered(true), null);
    }

    public static void run(Class clazz) {
        run(clazz, new VertxOptions().setClustered(false), null);
    }

    public static void run(Class clazz, VertxOptions options, DeploymentOptions deploymentOptions) {
        run(JAVA_DIR + clazz.getPackage().getName().replace(".", "/"), clazz.getName(), options, deploymentOptions);
    }

    public static void run(String cwd, String verticleID, VertxOptions options, DeploymentOptions deploymentOptions) {
        System.setProperty("vertx.cwd", cwd);

        if (options == null) {
            // Default parameter
            options = new VertxOptions();
        }

        Consumer<Vertx> runner = vertx -> {
            try {
                if (deploymentOptions != null) {
                    vertx.deployVerticle(verticleID, deploymentOptions);
                } else {
                    vertx.deployVerticle(verticleID);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        };

        if (options.isClustered()) {
            Vertx.clusteredVertx(options, res -> {
                if (res.succeeded()) {
                    Vertx vertx = res.result();
                    runner.accept(vertx);
                } else {
                    res.cause().printStackTrace();
                }
            });
        } else {
            Vertx vertx = Vertx.vertx(options);
            runner.accept(vertx);
        }
    }
}
