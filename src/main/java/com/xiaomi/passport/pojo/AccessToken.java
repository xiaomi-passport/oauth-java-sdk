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

import com.xiaomi.passport.constant.GlobalConstants;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * access token
 *
 * @author zhenchao.wang 2017-04-14 14:46:38
 * @version 1.0.0
 */
public class AccessToken {

    public enum TokenType {

        MAC("mac"),

        BEARER("bearer");

        private String type;

        TokenType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        /**
         * tell whether {@param tokenType} is valid
         *
         * @param tokenType
         * @return
         */
        public static boolean isValid(TokenType tokenType) {
            return MAC.equals(tokenType) || BEARER.equals(tokenType);
        }
    }

    /** access token */
    private String token;

    private String refreshToken;

    private String scope;

    private Long expiresIn;

    private String tokenType;

    private String macKey;

    private String macAlgorithm;

    public AccessToken(JSONObject json) {
        this.tokenType = json.getString(GlobalConstants.TOKEN_TYPE);
        this.scope = json.getString(GlobalConstants.SCOPE);
        this.token = json.getString(GlobalConstants.ACCESS_TOKEN);
        if (TokenType.MAC.getType().equals(tokenType)) {
            this.macKey = json.getString(GlobalConstants.MAC_KEY);
            this.macAlgorithm = json.getString(GlobalConstants.MAC_ALGORITHM);
        }
        this.expiresIn = json.getLong(GlobalConstants.EXPIRES_IN);
        this.refreshToken = json.getString(GlobalConstants.REFRESH_TOKEN);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public String getToken() {
        return token;
    }

    public AccessToken setToken(String token) {
        this.token = token;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public AccessToken setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public AccessToken setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public AccessToken setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }

    public AccessToken setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public String getMacKey() {
        return macKey;
    }

    public AccessToken setMacKey(String macKey) {
        this.macKey = macKey;
        return this;
    }

    public String getMacAlgorithm() {
        return macAlgorithm;
    }

    public AccessToken setMacAlgorithm(String macAlgorithm) {
        this.macAlgorithm = macAlgorithm;
        return this;
    }
}
