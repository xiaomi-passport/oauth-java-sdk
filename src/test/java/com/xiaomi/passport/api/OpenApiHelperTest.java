package com.xiaomi.passport.api;

import com.xiaomi.passport.pojo.Client;
import com.xiaomi.passport.pojo.UserProfile;
import com.xiaomi.passport.utils.OAuthTestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author zhenchao.wang 2017-04-21 11:44
 * @version 1.0.0
 */
public class OpenApiHelperTest {

    private Client client;

    private String accessToken = "your access token here";

    private OpenApiHelper helper;

    @Before
    public void setUp() throws Exception {
        client = OAuthTestUtils.getTestClient();
        helper = new OpenApiHelper(client.getId(), accessToken);
    }

    @Test
    public void getUserProfile() throws Exception {
        UserProfile profile = helper.getUserProfile();
        Assert.assertNotNull(profile);
        Assert.assertTrue(profile.getUserId() > 0);
        Assert.assertTrue(StringUtils.isNotBlank(profile.getNickname()));
        Assert.assertTrue(StringUtils.isNotBlank(profile.getAvatarUrl()));
    }

    @Test
    public void getOpenId() throws Exception {
        Assert.assertTrue(StringUtils.isNotBlank(helper.getOpenId()));
    }

    @Test
    @Deprecated
    public void getPhoneAndEmail() throws Exception {
        Pair<String, String> pair = helper.getPhoneAndEmail();
        Assert.assertNotNull(pair);
        Assert.assertTrue(StringUtils.isNotBlank(pair.getLeft()));
        Assert.assertTrue(StringUtils.isNotBlank(pair.getRight()));
    }

    @Test
    @Deprecated
    public void getFriendIdList() throws Exception {
        List<Long> friends = helper.getFriendIdList();
        Assert.assertTrue(CollectionUtils.isNotEmpty(friends));
    }

}