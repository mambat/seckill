package com.github.mambat.seckill;

import com.github.mambat.seckill.utils.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * Launcher for Application.
 *
 * @author wanglei
 * @date 17/2/22 上午11:43
 */
public class Launcher extends AbstractVerticle {
    private static int port = 8080;
    public static final String OPEN_ID = "openid";

    public static void main(String[] args) {
        parseArgs(args);
        Runner.run(Launcher.class);
    }

    private static void parseArgs(String[] args) {
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
                System.exit(1);
            }
        }
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.get("/api/apply/:id").handler(this::handleSeckillApply);
        router.get("/api/query/:id").handler(this::handleSeckillQuery);
        vertx.createHttpServer().requestHandler(router::accept).listen(port);
    }

    private void handleSeckillQuery(RoutingContext ctx) {
        String openid = ctx.request().getHeader(OPEN_ID);
    }

    private void handleSeckillApply(RoutingContext ctx) {

    }


}
