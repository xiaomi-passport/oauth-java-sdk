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

import com.xiaomi.passport.common.HttpMethod;
import com.xiaomi.passport.common.HttpRequestClient;
import com.xiaomi.passport.exception.OAuthSdkException;
import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * xiaomi passport open api request helper
 *
 * @author zhenchao.wang 2017-04-14 16:29:27
 * @version 1.0.0
 */
public class OpenApiHelper {

    private static final Logger log = LoggerFactory.getLogger(OpenApiHelper.class);

    private static final String OPEN_API_HOST = "https://open.account.xiaomi.com";

    protected String accessToken;

    protected long clientId;

    protected HttpRequestClient httpClient;

    public OpenApiHelper(String accessToken, long clientId) {
        this.accessToken = accessToken;
        this.clientId = clientId;
        this.httpClient = new HttpRequestClient();
    }

    public OpenApiHelper(long clientId, String accessToken, HttpRequestClient httpClient) {
        super();
        this.clientId = clientId;
        this.accessToken = accessToken;
        this.httpClient = httpClient;
    }

    /**
     * send request to specify url with params
     *
     * @param path the path part of api url, /user/profile etc.
     * @param method GET/POST
     * @param params query params
     * @return
     * @throws OAuthSdkException
     * @throws URISyntaxException
     */
    public JSONObject request(String path, HttpMethod method, List<NameValuePair> params)
            throws OAuthSdkException, URISyntaxException {
        return this.request(path, method, params, null);
    }

    /**
     * send request to specify url with params
     *
     * @param path the path part of api url, /user/profile etc.
     * @param method GET/POST
     * @param params query params
     * @param headers query http headers
     * @return
     * @throws OAuthSdkException
     * @throws URISyntaxException
     */
    public JSONObject request(String path, HttpMethod method, List<NameValuePair> params, List<Header> headers)
            throws OAuthSdkException, URISyntaxException {

        // param format
        params = (null == params) ? new ArrayList<NameValuePair>() : params;
        headers = (null == headers) ? new ArrayList<Header>() : headers;

        Header[] headerArray = new Header[headers.size()];
        headerArray = headers.toArray(headerArray);
        if (HttpMethod.GET.equals(method)) {
            return JSONObject.fromObject(httpClient.get(OPEN_API_HOST + path, params, headerArray));
        } else if (HttpMethod.POST.equals(method)) {
            return JSONObject.fromObject(httpClient.post(OPEN_API_HOST + path, params, headerArray));
        } else {
            throw new OAuthSdkException("Unknown http method!");
        }

    }

    public String getAccessToken() {
        return accessToken;
    }

    public OpenApiHelper setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public long getClientId() {
        return clientId;
    }

    public OpenApiHelper setClientId(long clientId) {
        this.clientId = clientId;
        return this;
    }

    public HttpRequestClient getHttpClient() {
        return httpClient;
    }

    public OpenApiHelper setHttpClient(HttpRequestClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }
}
