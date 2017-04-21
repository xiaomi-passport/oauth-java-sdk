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

package com.xiaomi.passport.util;

import com.xiaomi.passport.common.ResponseType;
import com.xiaomi.passport.constant.GlobalConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * authorize url util
 *
 * @author zhenchao.wang 2017-04-14 19:00
 * @version 1.0.0
 */
public class AuthorizeUrlUtils implements GlobalConstants {

    private AuthorizeUrlUtils() {
    }

    /**
     * get authorization code authorize url
     *
     * @param clientId
     * @param redirectUri
     * @return
     */
    public static String getCodeAuthorizeUrl(long clientId, String redirectUri) {
        return getAuthorizeUrl(ResponseType.AUTHORIZATION_CODE, clientId, redirectUri, null, null);
    }

    /**
     * get authorization code authorize url
     *
     * @param clientId
     * @param redirectUri
     * @param scope
     * @return
     */
    public static String getCodeAuthorizeUrl(long clientId, String redirectUri, String scope) {
        return getAuthorizeUrl(ResponseType.AUTHORIZATION_CODE, clientId, redirectUri, scope, null);
    }

    /**
     * get authorize url
     *
     * @param responseType code or token
     * @param clientId
     * @param redirectUri
     * @param scope
     * @param state
     * @return
     */
    public static String getAuthorizeUrl(
            ResponseType responseType, long clientId, String redirectUri, String scope, String state) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(CLIENT_ID, String.valueOf(clientId)));
        params.add(new BasicNameValuePair(RESPONSE_TYPE, responseType.getType()));
        if (StringUtils.isNotEmpty(scope)) {
            params.add(new BasicNameValuePair(SCOPE, scope));
        }
        if (StringUtils.isNotEmpty(state)) {
            params.add(new BasicNameValuePair(STATE, state));
        }
        params.add(new BasicNameValuePair(REDIRECT_URI, redirectUri));
        String query = URLEncodedUtils.format(params, GlobalConstants.DEFAULT_CHARSET);
        return String.format("%s?%s", getAuthorizeUrl(), query);
    }

    /**
     * get authorize endpoint url
     *
     * @return
     */
    public static String getAuthorizeUrl() {
        return OAUTH2_HOST + AUTHORIZE_PATH;
    }

    /**
     * get token endpoint url
     *
     * @return
     */
    public static String getTokenUrl() {
        return OAUTH2_HOST + TOKEN_PATH;
    }
}
