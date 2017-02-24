package com.github.mambat.seckill;

import com.github.mambat.seckill.auth.WXAuth;
import com.github.mambat.seckill.handler.SKApplyHandler;
import com.github.mambat.seckill.handler.WXHandler;
import com.github.mambat.seckill.utils.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * Application.
 *
 * @author wanglei
 * @date 17/2/22 上午11:43
 */
public class Application extends AbstractVerticle {

    private static int port = 8080;

    private RedisClient redisClient;

    public static void main(String[] args) {
        parseArgs(args);
        Runner.run(Application.class);
    }

    private static void parseArgs(String[] args) {
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument " + args[0] + " must be an integer.");
                System.exit(1);
            }
        }
    }

    @Override
    public void start() throws Exception {
        setupRedisClient();

        Router router = Router.router(vertx);

        AuthProvider authProvider = WXAuth.create();
        router.route().handler(WXHandler.create(authProvider));

        router.get("/api/apply/:id").handler(new SKApplyHandler(redisClient));

        vertx.createHttpServer().requestHandler(router::accept).listen(port);
    }

    private void setupRedisClient() {
        RedisOptions config = new RedisOptions().setHost("127.0.0.1").setPort(6379);
        this.redisClient = RedisClient.create(vertx, config);
    }
}
