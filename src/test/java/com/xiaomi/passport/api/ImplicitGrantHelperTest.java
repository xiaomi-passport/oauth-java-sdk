package com.xiaomi.passport.api;

import com.xiaomi.passport.pojo.Client;
import com.xiaomi.passport.utils.OAuthTestUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author zhenchao.wang 2017-04-21 11:42
 * @version 1.0.0
 */
public class ImplicitGrantHelperTest {

    private Client client;

    private ImplicitGrantHelper helper;

    @Before
    public void setUp() throws Exception {
        client = OAuthTestUtils.getTestClient();
        helper = new ImplicitGrantHelper(client);
    }

    @Test
    @Ignore
    public void getImplicitAccessToken() throws Exception {
        helper.getImplicitAccessToken(false, StringUtils.EMPTY);
    }

}