package com.xiaomi.passport.utils;

import com.xiaomi.passport.pojo.Client;

/**
 * @author zhenchao.wang 2017-04-20 16:39
 * @version 1.0.0
 */
public class OAuthTestUtils {

    private OAuthTestUtils() {
    }

    public static Client getTestClient() {
        Client client = new Client();
        // initialize your app info
        return client;
    }

}
