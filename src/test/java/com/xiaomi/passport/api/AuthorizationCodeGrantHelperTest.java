package com.xiaomi.passport.api;

import com.xiaomi.passport.pojo.Client;
import com.xiaomi.passport.utils.OAuthTestUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhenchao.wang 2017-04-20 16:30
 * @version 1.0.0
 */
public class AuthorizationCodeGrantHelperTest {

    private Client client;

    private AuthorizationCodeGrantHelper helper;

    @Before
    public void setUp() throws Exception {
        client = OAuthTestUtils.getTestClient();
        helper = new AuthorizationCodeGrantHelper(client);
    }

    @Test
    public void getAuthorizationCode() throws Exception {
        helper.getAuthorizationCode(false, StringUtils.EMPTY);
    }

}