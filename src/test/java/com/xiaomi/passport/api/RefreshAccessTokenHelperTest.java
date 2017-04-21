package com.xiaomi.passport.api;

import com.xiaomi.passport.exception.OAuthSdkException;
import com.xiaomi.passport.pojo.AccessToken;
import com.xiaomi.passport.pojo.Client;
import com.xiaomi.passport.utils.OAuthTestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhenchao.wang 2017-04-21 11:43
 * @version 1.0.0
 */
public class RefreshAccessTokenHelperTest {

    private Client client;

    private RefreshAccessTokenHelper helper;

    @Before
    public void setUp() throws Exception {
        client = OAuthTestUtils.getTestClient();
        helper = new RefreshAccessTokenHelper(client);
    }

    @Test
    public void refreshAccessTokenWithErrorTest() throws Exception {
        String refreshToken = RandomStringUtils.randomAlphanumeric(32);
        try {
            helper.refreshAccessToken(refreshToken);
            Assert.fail();
        } catch (OAuthSdkException e) {
            Assert.assertEquals(96009, e.getErrorCode());
        }
    }

    @Test
    public void refreshAccessTokenTest() throws Exception {
        String refreshToken = "your refresh token here";
        try {
            AccessToken accessToken = helper.refreshAccessToken(refreshToken);
            Assert.assertNotNull(accessToken);
            System.out.println("access token : " + accessToken);
            Assert.assertTrue(StringUtils.isNotBlank(accessToken.getToken()));
            Assert.assertEquals(AccessToken.TokenType.MAC.getType(), accessToken.getTokenType());
            Assert.assertTrue(StringUtils.isNotBlank(accessToken.getOpenId()));
            Assert.assertTrue(StringUtils.isNotBlank(accessToken.getRefreshToken()));
        } catch (OAuthSdkException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}