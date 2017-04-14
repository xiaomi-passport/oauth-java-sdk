/***************************************************************************
 *
 * Copyright (c) 2013 xiaomi.com, Inc. All Rights Reserved
 *
 **************************************************************************/

package com.xiaomi.passport.pojo;

import com.xiaomi.passport.constant.GlobalConstants;
import net.sf.json.JSONObject;

/**
 * access token
 *
 * @author zhenchao.wang 2017-04-14 14:46:38
 * @version 1.0.0
 */
public class AccessToken {

    public static final String MAC_TYPE = "mac";

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
        if (MAC_TYPE.equals(tokenType)) {
            this.macKey = json.getString(GlobalConstants.MAC_KEY);
            this.macAlgorithm = json.getString(GlobalConstants.MAC_ALGORITHM);
        }
        this.expiresIn = json.getLong(GlobalConstants.EXPIRES_IN);
        this.refreshToken = json.getString(GlobalConstants.REFRESH_TOKEN);
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
