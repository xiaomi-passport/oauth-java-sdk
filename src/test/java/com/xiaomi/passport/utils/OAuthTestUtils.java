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
        client.setId(2882303761517569294L).setName("小米测试").setKey("5601756982294").setSecret("264cCbeCLgT6wy5NigUk0w==")
                .setScopes(new Integer[] {1, 3}).setRedirectUri("https://account.xiaomi.com").setPackageName("com.xiaomi.passport.test");
        return client;
    }

}
