/***************************************************************************
 *
 * Copyright (c) 2013 xiaomi.com, Inc. All Rights Reserved
 *
 **************************************************************************/

package com.xiaomi.passport.api;

import com.xiaomi.passport.common.HttpRequestClient;
import com.xiaomi.passport.constant.GlobalConstants;
import com.xiaomi.passport.exception.XMException;
import com.xiaomi.passport.pojo.AccessToken;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
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
     * @param responseType code/token
     * @param state 可选参数，可以防止csrf攻击
     * @param scope 可选参数，是申请到scope的一个子集,用空格分割
     * @return 获取Authorize code Url
     */
    public String getAuthorizeUrl(String responseType, String state, String scope) {

        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair(CLIENT_ID, String.valueOf(clientId)));
        params.add(new BasicNameValuePair("response_type", responseType));
        if (StringUtils.isNotEmpty(scope)) {
            params.add(new BasicNameValuePair("scope", scope));
        }
        if (StringUtils.isNotEmpty(state)) {
            params.add(new BasicNameValuePair("state", state));
        }

        params.add(new BasicNameValuePair("redirect_uri", redirectUri));
        String query = URLEncodedUtils.format(params, GlobalConstants.DEFAULT_CHARSET);
        return getAuthorizeEndpoint() + "?" + query;
    }

    /**
     * 用Authorization Code换取access token
     *
     * @param code 服务器下发的Authorization Code
     * @return 获取到的AccessToken Object 或者 null
     * @throws XMException
     * @throws URISyntaxException
     */
    public AccessToken getAccessTokenByAuthorizationCode(String code) throws XMException, URISyntaxException {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("client_id", String.valueOf(clientId)));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("client_secret", clientSecret));
        params.add(new BasicNameValuePair("token_type", "mac"));
        params.add(new BasicNameValuePair("redirect_uri", redirectUri));
        params.add(new BasicNameValuePair("code", code));
        String result = httpClient.get(getTokenEndpoint(), params);
        log.debug("Get authorization code access token result[{}]", result);
        JSONObject json = JSONObject.fromObject(result);
        if (json.has("access_token")) {
            // FIXME 这里应该细化一下 2017-04-14 17:06:14
            return new AccessToken(json);
        } else {
            throw new XMException("No 'access_token' element in response!");
        }
    }

    /**
     * 用Refresh Token换取access token
     *
     * @param refreshToken 服务器下发的refresh_token
     * @return 获取到的AccessToken Object 或者 null
     * @throws XMException
     * @throws URISyntaxException
     */
    public AccessToken getAccessTokenByRefreshToken(String refreshToken) throws XMException, URISyntaxException {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("client_id", String.valueOf(clientId)));
        params.add(new BasicNameValuePair("grant_type", "refresh_token"));
        params.add(new BasicNameValuePair("client_secret", clientSecret));
        params.add(new BasicNameValuePair("token_type", "mac"));
        params.add(new BasicNameValuePair("redirect_uri", redirectUri));
        params.add(new BasicNameValuePair("refresh_token", refreshToken));
        String result = httpClient.get(getTokenEndpoint(), params);
        log.debug("Refresh access token result[{}]", result);
        JSONObject json = JSONObject.fromObject(result);
        if (json.has("access_token")) {
            // FIXME 这里应该细化一下 2017-04-14 17:06:14
            return new AccessToken(json);
        } else {
            throw new XMException("No 'access_token' element in response!");
        }
    }

    public String getAuthorizeUrl() {
        return getAuthorizeUrl("code", null, null);
    }

    public String getAuthorizeUrl(String state) {
        return getAuthorizeUrl("code", state, null);
    }

    public static String getAuthorizeEndpoint() {
        return OAUTH2_HOST + AUTHORIZE_PATH;
    }

    public static String getTokenEndpoint() {
        return OAUTH2_HOST + TOKEN_PATH;
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
