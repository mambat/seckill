package com.github.mambat.seckill.handler.impl;

import com.github.mambat.seckill.handler.WXHandler;
import com.github.mambat.seckill.utils.Keys;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.AuthHandlerImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wanglei
 * @date 17/2/23 上午11:29
 */
public class WXHandlerImpl extends AuthHandlerImpl implements WXHandler {

    private static final Logger LOG = LoggerFactory.getLogger(WXHandlerImpl.class);

    public WXHandlerImpl(AuthProvider authProvider) {
        super(authProvider);
    }

    @Override
    public void handle(RoutingContext context) {
        User user = context.user();
        if (user != null) {
            // Already authenticated in, just authorise
            authorise(user, context);
            return;
        }

        final String openid = context.request().headers().get(Keys.OPEN_ID);

        if (StringUtils.isEmpty(openid)) {
            LOG.warn("No openid header was found");
            context.fail(401);
            return;
        }

        JsonObject authInfo = new JsonObject().put(Keys.OPEN_ID, openid);
        authProvider.authenticate(authInfo, res -> {
            if (res.succeeded()) {
                final User user2 = res.result();
                context.setUser(user2);
                authorise(user2, context);
            } else {
                LOG.warn("WX auth failure", res.cause());
                context.fail(401);
            }
        });
    }
}
