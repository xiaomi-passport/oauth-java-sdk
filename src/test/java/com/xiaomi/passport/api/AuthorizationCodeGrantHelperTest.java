package com.xiaomi.passport.api;

import com.xiaomi.passport.exception.OAuthSdkException;
import com.xiaomi.passport.pojo.AccessToken;
import com.xiaomi.passport.pojo.Client;
import com.xiaomi.passport.utils.OAuthTestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
    @Ignore
    public void getAuthorizationCodeTest() throws Exception {
        helper.getAuthorizationCode(false, StringUtils.EMPTY);
    }

    @Test
    public void getAccessTokenByCodeWithErrorTest() throws Exception {
        String errorCode = RandomStringUtils.randomAlphabetic(32);
        try {
            helper.getAccessTokenByCode(errorCode);
            Assert.fail();
        } catch (OAuthSdkException e) {
            e.printStackTrace();
            Assert.assertEquals(96013, e.getErrorCode());
        }
    }

    @Test
    public void getAccessTokenByCodeTest() throws Exception {
        String code = "your authorization code here";
        try {
            AccessToken accessToken = helper.getAccessTokenByCode(code);
            Assert.assertNotNull(accessToken);
            System.out.println("access token : " + accessToken);
            Assert.assertTrue(StringUtils.isNotBlank(accessToken.getToken()));
            Assert.assertEquals(AccessToken.TokenType.MAC.getType(), accessToken.getTokenType());
            Assert.assertTrue(StringUtils.isNotBlank(accessToken.getOpenId()));
            Assert.assertTrue(StringUtils.isNotBlank(accessToken.getRefreshToken()));
        } catch (OAuthSdkException e) {
            Assert.fail();
        }
    }

}