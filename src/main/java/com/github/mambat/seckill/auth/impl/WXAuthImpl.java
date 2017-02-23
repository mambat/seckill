package com.github.mambat.seckill.auth.impl;

import com.github.bingoohuang.utils.crypto.Aes;
import com.github.mambat.seckill.auth.WXAuth;
import com.github.mambat.seckill.utils.Keys;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
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

    // create a random key: Aes.generateKey(); -- "ZtqXzMQRYK4dGzdlW01aTQ"
    // create encrypt openid: Aes.encrypt("your_openid", Aes.getKey(AES_KEY); -- "q9MaqpGNKyRXHsu55E-jyA"
    private static final String AES_KEY = "ZtqXzMQRYK4dGzdlW01aTQ";

    @Override
    public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {
        String openid = authInfo.getString(Keys.OPEN_ID);
        if (StringUtils.isEmpty(openid)) {
            resultHandler.handle(Future.failedFuture("openid is required"));
            return;
        }

        try {
            String decryptOpenid = Aes.decrypt(openid, Aes.getKey(Base64.encode(AES_KEY.getBytes())));
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
