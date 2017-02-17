/***************************************************************************
 *
 * Copyright (c) 2013 xiaomi.com, Inc. All Rights Reserved
 *
 **************************************************************************/

package com.xiaomi.utils;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class AccessToken {

    public static final String MAC_TYPE = "mac";

    protected String accessTokenId;

    protected String refreshToken;

    protected String scope;

    protected long expiresIn;

    protected String tokenType;

    protected String macKey;

    protected String macAlgorithm;

    /**
     * @param json 返回的字符串，构造的JSONObject
     * @throws JSONException
     */
    public AccessToken(JSONObject json) throws JSONException {
        tokenType = json.getString("token_type");
        scope = json.getString("scope");
        accessTokenId = json.getString("access_token");
        if (StringUtils.equals(tokenType, MAC_TYPE)) {
            macKey = json.getString("mac_key");
            macAlgorithm = json.getString("mac_algorithm");
        }
        expiresIn = json.getLong("expires_in");
        refreshToken = json.getString("refresh_token");
    }

    public String getAccessTokenId() {
        return accessTokenId;
    }

    public void setAccessTokenId(String accessTokenId) {
        this.accessTokenId = accessTokenId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getMacKey() {
        return macKey;
    }

    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }

    public String getMacAlgorithm() {
        return macAlgorithm;
    }

    public void setMacAlgorithm(String macAlgorithm) {
        this.macAlgorithm = macAlgorithm;
    }
}
