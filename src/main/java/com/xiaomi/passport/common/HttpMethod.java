package com.xiaomi.passport.common;

/**
 * http method
 *
 * @author zhenchao.wang 2017-04-14 16:36
 * @version 1.0.0
 */
public enum HttpMethod {

    GET("GET"),

    POST("POST");

    private String value;

    HttpMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
