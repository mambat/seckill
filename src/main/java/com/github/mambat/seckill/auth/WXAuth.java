package com.github.mambat.seckill.auth;

import com.github.mambat.seckill.auth.impl.WXAuthImpl;
import io.vertx.ext.auth.AuthProvider;

/**
 * Customized Auth Provider for Wechat.
 *
 * @author wanglei
 * @date 17/2/22 下午9:32
 */
public interface WXAuth extends AuthProvider {

    static WXAuth create() {
        return new WXAuthImpl();
    }
}
