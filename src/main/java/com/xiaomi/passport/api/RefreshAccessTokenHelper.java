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
import com.xiaomi.passport.constant.GlobalConstants;
import com.xiaomi.passport.exception.OAuthSdkException;
import com.xiaomi.passport.pojo.AccessToken;
import com.xiaomi.passport.pojo.Client;
import com.xiaomi.passport.util.AuthorizeUrlUtils;
import com.xiaomi.passport.util.HttpResponseUtils;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * refresh access token
 *
 * @author zhenchao.wang 2017-04-20 18:12
 * @version 1.0.0
 */
public class RefreshAccessTokenHelper implements GlobalConstants {

    private static final Logger log = LoggerFactory.getLogger(RefreshAccessTokenHelper.class);

    private Client client;

    private OAuthHttpClient httpClient;

    public RefreshAccessTokenHelper(Client client) {
        this.client = client;
        this.httpClient = new OAuthHttpClient();
    }

    public RefreshAccessTokenHelper(Client client, OAuthHttpClient httpClient) {
        this.client = client;
        this.httpClient = httpClient;
    }

    /**
     * refresh access token by refresh token
     *
     * @param refreshToken
     * @return
     * @throws OAuthSdkException
     */
    public AccessToken refreshAccessToken(String refreshToken) throws OAuthSdkException {

        // prepare params
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(CLIENT_ID, String.valueOf(client.getId())));
        params.add(new BasicNameValuePair(REDIRECT_URI, client.getRedirectUri()));
        params.add(new BasicNameValuePair(CLIENT_SECRET, client.getSecret()));
        params.add(new BasicNameValuePair(GRANT_TYPE, GrantType.REFRESH_TOKEN.getType()));
        params.add(new BasicNameValuePair(TOKEN_TYPE, AccessToken.TokenType.MAC.getType()));
        params.add(new BasicNameValuePair(REFRESH_TOKEN, refreshToken));

        HttpResponse response = httpClient.get(AuthorizeUrlUtils.getTokenUrl(), params);
        log.debug("Refresh access token response[{}]", response);

        String entityContent = HttpResponseUtils.getEntityContent(response);
        if (StringUtils.isBlank(entityContent) || !JSONUtils.mayBeJSON(entityContent)) {
            log.error("The refresh token response[{}] is not json format！", entityContent);
            throw new OAuthSdkException("The refresh token response is not json format");
        }

        JSONObject json = JSONObject.fromObject(entityContent);
        if (json.has("access_token")) {
            log.debug("Refresh access token json result[{}]", json);
            return new AccessToken(json);
        }

        // error response
        int errorCode = json.optInt("error", -1);
        String errorDesc = json.optString("error_description", StringUtils.EMPTY);
        log.error("Refresh access token error, error info [code={}, desc={}]!", errorCode, errorDesc);
        throw new OAuthSdkException("Refresh access token error!", errorCode, errorDesc);
    }

}
