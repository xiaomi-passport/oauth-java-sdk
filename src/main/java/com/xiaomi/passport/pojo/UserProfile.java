/*
 * Copyright (c) 2013-2017 xiaomi.com, Inc. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaomi.passport.pojo;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * xiaomi user profile
 *
 * @author zhenchao.wang 2017-04-20 18:29
 * @version 1.0.0
 */
public class UserProfile {

    private Long userId;

    private String openId;

    private String nickname;

    private String avatarUrl;

    private String phoneNumber;

    private String email;

    private List<Long> friends = new ArrayList<Long>();

    public UserProfile() {
    }

    public UserProfile(JSONObject json) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(json.optLong("userId", -1L))
                .setNickname(json.optString("miliaoNick", StringUtils.EMPTY))
                .setAvatarUrl(json.optString("miliaoIcon", StringUtils.EMPTY))
                .setOpenId(json.optString("openid", StringUtils.EMPTY))
                .setPhoneNumber(json.optString("phone", StringUtils.EMPTY))
                .setEmail(json.optString("email", StringUtils.EMPTY));
        String friends = json.optString("friends", StringUtils.EMPTY);
        if (StringUtils.isNotBlank(friends)) {
            List<Long> ids = new ArrayList<Long>();
            String[] elements = friends.split(",");
            for (final String element : elements) {
                ids.add(Long.valueOf(element));
            }
            userProfile.setFriends(ids);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public Long getUserId() {
        return userId;
    }

    public UserProfile setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getOpenId() {
        return openId;
    }

    public UserProfile setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public UserProfile setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public UserProfile setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserProfile setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserProfile setEmail(String email) {
        this.email = email;
        return this;
    }

    public List<Long> getFriends() {
        return friends;
    }

    public UserProfile setFriends(List<Long> friends) {
        this.friends = friends;
        return this;
    }
}
