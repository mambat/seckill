package com.github.mambat.seckill.handler;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.redis.RedisClient;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;

/**
 * TODO: Document Me!!
 *
 * @author wanglei
 * @date 17/2/24 上午11:02
 */
public class SKApplyHandler implements Handler<RoutingContext> {

    private static final String REDIS_KEY_SK = "sk:";

    private static final String REDIS_KEY_SK_COUNT = "sk:count:";

    private final RedisClient redisClient;

    public SKApplyHandler(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public void handle(RoutingContext context) {
        String skId = context.pathParam("id");
        redisClient.get(REDIS_KEY_SK + skId, res -> {
            if (res.succeeded()) {
                if (StringUtils.isBlank(res.result())) {
                    notFound(context);
                }

                JsonObject sk = new JsonObject(res.result());
                if (!isOpen(sk)) {
                    notFound(context);
                }

                HttpServerResponse response = context.response();
                response.putHeader("content-type", "text/plain");
                // Write to the response and end it
                response.end(res.result());
            } else {
                notFound(context);
            }
        });
    }

    private boolean isOpen(JsonObject sk) {
        Long start = sk.getLong("sat");
        Long end = sk.getLong("eat");
        long now = Calendar.getInstance().getTimeInMillis();
        return (start <= now) && (now <= end);
    }

    private void notFound(RoutingContext context) {
        context.fail(404);
    }
}
