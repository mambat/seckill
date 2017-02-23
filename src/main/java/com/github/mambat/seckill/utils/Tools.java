package com.github.mambat.seckill.utils;

import com.github.bingoohuang.utils.crypto.Aes;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.tuple.Pair;

/**
 * TODO: Document Me!!
 *
 * @author wanglei
 * @date 17/2/23 下午11:55
 */
public class Tools {

    public static Pair<String, String> createAuthData(String openid) {
        String aesKey = Aes.generateKey();
        JsonObject openidObject = new JsonObject().put(Keys.OPEN_ID, openid);
        String encryptOpenid = Aes.encrypt(openidObject.encode(), Aes.getKey(aesKey));
        return Pair.of(aesKey, encryptOpenid);
    }
}
