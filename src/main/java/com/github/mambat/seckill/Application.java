package com.github.mambat.seckill;

import com.github.mambat.seckill.auth.WXAuth;
import com.github.mambat.seckill.handler.WXHandler;
import com.github.mambat.seckill.utils.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * Application.
 *
 * @author wanglei
 * @date 17/2/22 上午11:43
 */
public class Application extends AbstractVerticle {
    private static int port = 8080;
    public static final String OPEN_ID = "openid";

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
        Router router = Router.router(vertx);

        AuthProvider authProvider = WXAuth.create();
        router.route().handler(WXHandler.create(authProvider));

        router.get("/seckill/apply/:id").handler(this::handleSeckill);
        vertx.createHttpServer().requestHandler(router::accept).listen(port);
    }

    private void handleSeckill(RoutingContext context) {
        String openid = context.request().getHeader(OPEN_ID);
//        BasicAuthHandler.create(authProvider);
        // This handler will be called for every request
        HttpServerResponse response = context.response();
        response.putHeader("content-type", "text/plain");

        // Write to the response and end it
        response.end("Hello " + context.user().toString());
    }
}
