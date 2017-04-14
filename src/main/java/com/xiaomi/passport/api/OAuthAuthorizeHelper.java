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

package com.xiaomi.passport.api;

import com.xiaomi.passport.common.HttpRequestClient;
import com.xiaomi.passport.constant.GlobalConstants;
import com.xiaomi.passport.exception.OAuthSdkException;
import com.xiaomi.passport.pojo.AccessToken;
import com.xiaomi.passport.util.AuthorizeUrlUtils;
import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

/**
 * oauth authorize api
 *
 * @author zhenchao.wang 2017-04-14 13:48:14
 * @version 1.0.0
 */
public class OAuthAuthorizeHelper implements GlobalConstants {

    private static final Logger log = LoggerFactory.getLogger(OAuthAuthorizeHelper.class);

    protected long clientId;

    protected String clientSecret;

    protected String redirectUri;

    protected HttpRequestClient httpClient;

    public OAuthAuthorizeHelper(long clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.httpClient = new HttpRequestClient();
    }

    public OAuthAuthorizeHelper(long clientId, String clientSecret, String redirectUri, HttpRequestClient httpClient) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.httpClient = httpClient;
    }

    /**
     * 用Authorization Code换取access token
     *
     * @param code 服务器下发的Authorization Code
     * @return 获取到的AccessToken Object 或者 null
     * @throws OAuthSdkException
     * @throws URISyntaxException
     */
    public AccessToken getAccessTokenByAuthorizationCode(String code) throws OAuthSdkException, URISyntaxException {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair(CLIENT_ID, String.valueOf(clientId)));
        params.add(new BasicNameValuePair(GRANT_TYPE, "authorization_code"));
        params.add(new BasicNameValuePair(CLIENT_SECRET, clientSecret));
        params.add(new BasicNameValuePair(TOKEN_TYPE, "mac"));
        params.add(new BasicNameValuePair(REDIRECT_URI, redirectUri));
        params.add(new BasicNameValuePair(CODE, code));
        String result = httpClient.get(AuthorizeUrlUtils.getTokenEndpoint(), params);
        log.debug("Get authorization code access token result[{}]", result);
        JSONObject json = JSONObject.fromObject(result);
        if (json.has("access_token")) {
            // FIXME 这里应该细化一下 2017-04-14 17:06:14
            return new AccessToken(json);
        } else {
            throw new OAuthSdkException("No 'access_token' element in response!");
        }
    }

    /**
     * 用Refresh Token换取access token
     *
     * @param refreshToken 服务器下发的refresh_token
     * @return 获取到的AccessToken Object 或者 null
     * @throws OAuthSdkException
     * @throws URISyntaxException
     */
    public AccessToken getAccessTokenByRefreshToken(String refreshToken) throws OAuthSdkException, URISyntaxException {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair(CLIENT_ID, String.valueOf(clientId)));
        params.add(new BasicNameValuePair(GRANT_TYPE, "refresh_token"));
        params.add(new BasicNameValuePair(CLIENT_SECRET, clientSecret));
        params.add(new BasicNameValuePair(TOKEN_TYPE, "mac"));
        params.add(new BasicNameValuePair(REDIRECT_URI, redirectUri));
        params.add(new BasicNameValuePair(REFRESH_TOKEN, refreshToken));
        String result = httpClient.get(AuthorizeUrlUtils.getTokenEndpoint(), params);
        log.debug("Refresh access token result[{}]", result);
        JSONObject json = JSONObject.fromObject(result);
        if (json.has("access_token")) {
            // FIXME 这里应该细化一下 2017-04-14 17:06:14
            return new AccessToken(json);
        } else {
            throw new OAuthSdkException("No 'access_token' element in response!");
        }
    }

    // getter and setter

    public long getClientId() {
        return clientId;
    }

    public OAuthAuthorizeHelper setClientId(long clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public OAuthAuthorizeHelper setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public OAuthAuthorizeHelper setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }
}
