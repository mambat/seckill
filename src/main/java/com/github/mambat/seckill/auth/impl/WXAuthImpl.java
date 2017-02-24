package com.github.mambat.seckill.auth.impl;

import com.github.bingoohuang.utils.crypto.Aes;
import com.github.mambat.seckill.auth.WXAuth;
import com.github.mambat.seckill.utils.Keys;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wanglei
 * @date 17/2/22 下午9:37
 */
public class WXAuthImpl implements WXAuth, AuthProvider {

    private static final Logger LOG = LoggerFactory.getLogger(WXAuthImpl.class);

    // Tools.createAuthData();
    // V5bfx2ego8Y9qsCt6OeytQi_loagHY1LWQI7eGRwh6M
    private static final String AES_KEY = "EpU1pnplroNnF3Bhg0shFA";

    @Override
    public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {
        String openid = authInfo.getString(Keys.OPEN_ID);
        if (StringUtils.isEmpty(openid)) {
            resultHandler.handle(Future.failedFuture("openid is required"));
            return;
        }

        try {
            String decryptOpenid = Aes.decrypt(openid, Aes.getKey(AES_KEY));
            JsonObject decryptOpenidObj = new JsonObject(decryptOpenid);

            if (StringUtils.isEmpty(decryptOpenidObj.getString(Keys.OPEN_ID))) {
                throw new RuntimeException("openid is required");
            }

            resultHandler.handle(Future.succeededFuture(new WXUser(decryptOpenidObj)));
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
            resultHandler.handle(Future.failedFuture("openid is invalid"));
        }
    }
}
