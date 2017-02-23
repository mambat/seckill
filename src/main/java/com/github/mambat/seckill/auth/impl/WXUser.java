package com.github.mambat.seckill.auth.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;

/**
 * TODO: Document Me!!
 *
 * @author wanglei
 * @date 17/2/23 下午4:02
 */
public class WXUser extends AbstractUser {

    private final JsonObject openid;

    public WXUser(JsonObject openid) {
        this.openid = openid;
    }

    @Override
    protected void doIsPermitted(String permission, Handler<AsyncResult<Boolean>> handler) {
        // NOOP - with openid, with all permissions :)
        handler.handle(Future.succeededFuture(true));
    }

    @Override
    public JsonObject principal() {
        return openid;
    }

    @Override
    public void setAuthProvider(AuthProvider authProvider) {
        // NOOP - WX tokens are self contained :)
    }
}
