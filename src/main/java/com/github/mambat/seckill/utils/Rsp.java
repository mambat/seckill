package com.github.mambat.seckill.utils;

/**
 * TODO: Document Me!!
 *
 * @author wanglei
 * @date 17/2/24 上午11:54
 */
public class Rsp {
    private String code;
    private String desc;

    public Rsp(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
