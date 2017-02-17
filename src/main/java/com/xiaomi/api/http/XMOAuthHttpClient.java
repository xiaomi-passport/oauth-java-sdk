/***************************************************************************
 *
 * Copyright (c) 2013 xiaomi.com, Inc. All Rights Reserved
 *
 **************************************************************************/

package com.xiaomi.api.http;

import com.xiaomi.utils.AccessToken;
import com.xiaomi.utils.XMUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class XMOAuthHttpClient {

    private static final Logger log = Logger.getLogger(XMOAuthHttpClient.class.getName());

    public static final String OAUTH2_HOST = "https://account.xiaomi.com";

    public static final String AUTHORIZE_PATH = "/oauth2/authorize";

    public static final String TOKEN_PATH = "/oauth2/token";

    protected long clientId;

    protected String clientSecret;

    protected String redirectUri;

    protected XMHttpClient xmHttpClient;

    public XMOAuthHttpClient(long clientId, String clientSecret, String redirectUri, XMHttpClient xmHttpClient) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.xmHttpClient = xmHttpClient;
    }

    /**
     * @param responseType code/token
     * @param state 可选参数，可以防止csrf攻击
     * @param scope 可选参数，是申请到scope的一个子集,用空格分割
     * @return 获取Authorize code Url
     */
    public String getAuthorizeUrl(String responseType, String state, String scope) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("client_id", String.valueOf(clientId)));
        params.add(new BasicNameValuePair("response_type", responseType));
        if (StringUtils.isNotEmpty(scope)) {
            params.add(new BasicNameValuePair("scope", scope));
        }
        if (StringUtils.isNotEmpty(state)) {
            params.add(new BasicNameValuePair("state", state));
        }
        params.add(new BasicNameValuePair("redirect_uri", redirectUri));
        String query = URLEncodedUtils.format(params, XMHttpClient.DEFAULT_CHARSET);
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
        String result = xmHttpClient.get(getTokenEndpoint(), params);
        XMUtil.Log(log, "Get access token response: " + result);
        try {
            JSONObject json = new JSONObject(result);
            if (json.has("access_token")) {
                return new AccessToken(json);
            } else {
                throw new XMException(json);
            }
        } catch (JSONException e) {
            XMUtil.Log(log, e.getMessage());
        }
        return null;
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
        String result = xmHttpClient.get(getTokenEndpoint(), params);
        XMUtil.Log(log, "Get access token response: " + result);
        try {
            JSONObject json = new JSONObject(result);
            if (json.has("access_token")) {
                return new AccessToken(json);
            } else {
                throw new XMException(json);
            }
        } catch (JSONException e) {
            XMUtil.Log(log, e.getMessage());
        }
        return null;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public static String getAuthorizeEndpoint() {
        return OAUTH2_HOST + AUTHORIZE_PATH;
    }

    public static String getTokenEndpoint() {
        return OAUTH2_HOST + TOKEN_PATH;
    }

    public String getAuthorizeUrl() {
        return getAuthorizeUrl("code", null, null);
    }

    public String getAuthorizeUrl(String state) {
        return getAuthorizeUrl("code", state, null);
    }
}
