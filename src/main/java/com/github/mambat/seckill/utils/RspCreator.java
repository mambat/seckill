package com.github.mambat.seckill.utils;

/**
 * TODO: Document Me!!
 *
 * @author wanglei
 * @date 17/2/24 上午11:55
 */
public class RspCreator {

    private static final String CODE_SUCCESS = "0";
    
    private static final String CODE_FAILURE = "1";

    public static Rsp success() {
        return new Rsp(CODE_SUCCESS, "ok");
    }

    public static Rsp failure(String desc) {
        return new Rsp(CODE_FAILURE, desc);
    }

    public static Rsp failure(Exception e) {
        return new Rsp(CODE_FAILURE, e.getMessage());
    }


}
