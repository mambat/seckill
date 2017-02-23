package com.github.mambat.seckill.handler;

import com.github.mambat.seckill.handler.impl.WXHandlerImpl;
import io.vertx.core.Handler;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.RoutingContext;

/**
 * TODO: Document Me!!
 *
 * @author wanglei
 * @date 17/2/23 上午11:29
 */
public interface WXHandler extends Handler<RoutingContext> {

    static WXHandler create(AuthProvider authProvider) {
        return new WXHandlerImpl(authProvider);
    }
}
