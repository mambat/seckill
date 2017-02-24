package com.github.mambat.seckill.handler;

import com.github.mambat.seckill.utils.Rsp;
import com.github.mambat.seckill.utils.RspCreator;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.redis.RedisClient;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;

/**
 * TODO: Fix callback hell.
 *
 * @author wanglei
 * @date 17/2/24 上午11:02
 */
public class SKApplyHandler implements Handler<RoutingContext> {

    private static final String REDIS_KEY_SK = "sk:";

    private static final String REDIS_KEY_SK_STOCK = "sk_stock:";

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
                    return;
                }

                JsonObject sk = new JsonObject(res.result());

                if (!isOpen(sk)) {
                    notFound(context);
                    return;
                }

                redisClient.decr(REDIS_KEY_SK_STOCK + skId, r -> {
                    if (r.succeeded()) {
                        if (r.result() < 0) {
                            end(context, RspCreator.failure("out of stock"));
                        } else {
                            // TODO: save seckill result
                            end(context, RspCreator.success());
                        }
                    }
                });
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

    private void end(RoutingContext context, Rsp rsp) {
        context.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encode(rsp));
    }
}
