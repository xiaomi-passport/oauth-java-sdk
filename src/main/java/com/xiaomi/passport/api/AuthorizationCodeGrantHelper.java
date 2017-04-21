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

import com.xiaomi.passport.common.GrantType;
import com.xiaomi.passport.common.OAuthHttpClient;
import com.xiaomi.passport.common.ResponseType;
import com.xiaomi.passport.constant.GlobalConstants;
import com.xiaomi.passport.exception.OAuthSdkException;
import com.xiaomi.passport.pojo.AccessToken;
import com.xiaomi.passport.pojo.Client;
import com.xiaomi.passport.util.AuthorizeUrlUtils;
import com.xiaomi.passport.util.HttpResponseUtils;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Authorization code grant type
 *
 * @author zhenchao.wang 2017-04-14 13:48:14
 * @version 1.0.0
 */
public class AuthorizationCodeGrantHelper implements GlobalConstants {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationCodeGrantHelper.class);

    private Client client;

    private OAuthHttpClient httpClient;

    public AuthorizationCodeGrantHelper(Client client) {
        this.client = client;
        this.httpClient = new OAuthHttpClient();
    }

    public AuthorizationCodeGrantHelper(Client client, OAuthHttpClient httpClient) {
        this.client = client;
        this.httpClient = httpClient;
    }

    /**
     * get authorization code
     * since issue authorization code process must interact with resource owner, so we can't do it only in program
     *
     * @param skipConfirm
     * @param state
     * @return
     * @deprecated this method is error to use
     */
    @Deprecated
    public String getAuthorizationCode(boolean skipConfirm, String state) {

        log.debug("Get authorization code...");

        // prepare params
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(CLIENT_ID, String.valueOf(client.getId())));
        params.add(new BasicNameValuePair(REDIRECT_URI, client.getRedirectUri()));
        params.add(new BasicNameValuePair(RESPONSE_TYPE, ResponseType.AUTHORIZATION_CODE.getType()));
        if (ArrayUtils.isNotEmpty(client.getScopes())) {
            params.add(new BasicNameValuePair(SCOPE, StringUtils.join(client.getScopes(), " ")));
        }
        if (StringUtils.isNotBlank(state)) {
            params.add(new BasicNameValuePair(STATE, state));
        }
        params.add(new BasicNameValuePair(SKIP_CONFIRM, String.valueOf(skipConfirm)));

        /*
         * interactive process：
         *
         * 1. request https://account.xiaomi.com/oauth2/authorize with params
         * 2. interact with resource owner and authorize
         * 3. issued authorization code as 302 redirect
         *
         */

        return StringUtils.EMPTY;
    }

    /**
     * get access code by authorization code
     *
     * @param code authorization code
     * @return
     * @throws OAuthSdkException when encounter error response
     */
    public AccessToken getAccessTokenByCode(String code) throws OAuthSdkException {

        log.debug("Get access token by code...");

        // prepare params
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(CLIENT_ID, String.valueOf(client.getId())));
        params.add(new BasicNameValuePair(REDIRECT_URI, client.getRedirectUri()));
        params.add(new BasicNameValuePair(CLIENT_SECRET, client.getSecret()));
        params.add(new BasicNameValuePair(GRANT_TYPE, GrantType.AUTHORIZATION_CODE.getType()));
        params.add(new BasicNameValuePair(CODE, code));
        params.add(new BasicNameValuePair(TOKEN_TYPE, AccessToken.TokenType.MAC.getType()));

        HttpResponse response = httpClient.get(AuthorizeUrlUtils.getTokenUrl(), params);
        log.debug("Get authorization code access token response[{}]", response);

        String entityContent = HttpResponseUtils.getEntityContent(response);
        if (StringUtils.isBlank(entityContent) || !JSONUtils.mayBeJSON(entityContent)) {
            log.error("The response[{}] is not json format！", entityContent);
            throw new OAuthSdkException("The response is not json format");
        }

        JSONObject json = JSONObject.fromObject(entityContent);
        if (json.has("access_token")) {
            log.debug("Get authorization code access token json result[{}]", json);
            return new AccessToken(json);
        }

        // error response
        int errorCode = json.optInt("error", -1);
        String errorDesc = json.optString("error_description", StringUtils.EMPTY);
        log.error("Get authorization code access token error, error info [code={}, desc={}]!", errorCode, errorDesc);
        throw new OAuthSdkException("Get authorization code access token error!", errorCode, errorDesc);
    }

}
