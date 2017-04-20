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
import com.xiaomi.passport.common.OAuthHttpClient;
import com.xiaomi.passport.constant.GlobalConstants;
import com.xiaomi.passport.exception.OAuthSdkException;
import com.xiaomi.passport.pojo.UserProfile;
import com.xiaomi.passport.util.HttpResponseUtils;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * xiaomi passport open api request helper
 * docs: <url>https://dev.mi.com/docs/passport/open-api/</url>
 *
 * @author zhenchao.wang 2017-04-14 16:29:27
 * @version 1.0.0
 */
public class OpenApiHelper implements GlobalConstants {

    protected static final Logger log = LoggerFactory.getLogger(OpenApiHelper.class);

    protected static final String OPEN_CLIENT_ID = "clientId";

    protected static final String ACCESS_TOKEN = "token";

    protected String accessToken;

    protected long clientId;

    protected OAuthHttpClient httpClient;

    public OpenApiHelper(String accessToken, long clientId) {
        super();
        this.accessToken = accessToken;
        this.clientId = clientId;
        this.httpClient = new OAuthHttpClient();
    }

    public OpenApiHelper(long clientId, String accessToken, OAuthHttpClient httpClient) {
        super();
        this.clientId = clientId;
        this.accessToken = accessToken;
        this.httpClient = httpClient;
    }

    /**
     * get user profile by access token and client id
     *
     * @return
     * @throws OAuthSdkException
     */
    public UserProfile getUserProfile() throws OAuthSdkException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(OPEN_CLIENT_ID, String.valueOf(clientId)));
        params.add(new BasicNameValuePair(ACCESS_TOKEN, accessToken));
        JSONObject json = this.request(HttpMethod.GET, USER_PROFILE_PATH, params);
        if (!this.isSuccessResult(json)) {
            // error response
            int errorCode = json.optInt("code", -1);
            String errorDesc = json.optString("description", StringUtils.EMPTY);
            log.error("Get user profile error, error info[code={}, desc={}]", errorCode, errorDesc);
            throw new OAuthSdkException("get user profile error", errorCode, errorDesc);
        }
        return new UserProfile(json.optJSONObject("data"));
    }

    /**
     * get open id by access token and client id
     *
     * @return
     * @throws OAuthSdkException
     */
    public String getOpenId() throws OAuthSdkException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(OPEN_CLIENT_ID, String.valueOf(clientId)));
        params.add(new BasicNameValuePair(ACCESS_TOKEN, accessToken));
        JSONObject json = this.request(HttpMethod.GET, OPEN_ID_PATH, params);
        if (!this.isSuccessResult(json)) {
            // error response
            int errorCode = json.optInt("code", -1);
            String errorDesc = json.optString("description", StringUtils.EMPTY);
            log.error("Get user open id error, error info[code={}, desc={}]", errorCode, errorDesc);
            throw new OAuthSdkException("get user profile error", errorCode, errorDesc);
        }
        return json.optJSONObject("data").optString("openid", StringUtils.EMPTY);
    }

    /**
     * get phone number and email address by access token and client id
     *
     * @return
     * @throws OAuthSdkException
     */
    public Pair<String, String> getPhoneAndEmail() throws OAuthSdkException {
        return null;
    }

    /**
     * get user miliao friend id list by access token and client id
     *
     * @return
     * @throws OAuthSdkException
     */
    public List<Long> getFriendIdList() throws OAuthSdkException {
        return null;
    }

    public void checkPassword() throws OAuthSdkException {

    }

    /**
     * send request to specify url with params and expected json response
     *
     * @param path the path part of api url, /user/profile etc.
     * @param method GET or POST
     * @param params query params
     * @return json response data
     * @throws OAuthSdkException
     */
    protected JSONObject request(HttpMethod method, String path, List<NameValuePair> params) throws OAuthSdkException {
        return this.request(method, path, params, null);
    }

    /**
     * send request to specify url with params and expected json response
     *
     * @param path the path part of api url, /user/profile etc.
     * @param method GET or POST
     * @param params query params
     * @param headers query http headers
     * @return json response data
     * @throws OAuthSdkException
     */
    protected JSONObject request(
            HttpMethod method, String path, List<NameValuePair> params, List<Header> headers) throws OAuthSdkException {

        // params format
        List<NameValuePair> paramList = (null == params) ? new ArrayList<NameValuePair>() : params;
        List<Header> headerList = (null == headers) ? new ArrayList<Header>() : headers;

        Header[] headerArray = new Header[0];
        if (CollectionUtils.isEmpty(headers)) {
            headerArray = new Header[headerList.size()];
            headerArray = headerList.toArray(headerArray);
        }

        HttpResponse response;
        if (HttpMethod.GET.equals(method)) {
            response = httpClient.get(GlobalConstants.OPEN_API_HOST + path, params, headerArray);
        } else if (HttpMethod.POST.equals(method)) {
            response = httpClient.post(GlobalConstants.OPEN_API_HOST + path, params, headerArray);
        } else {
            log.error("The http method [{}] is unsupported!", method);
            throw new OAuthSdkException("unsupported http method");
        }

        String entityContent = HttpResponseUtils.getEntityContent(response);
        if (StringUtils.isBlank(entityContent) || !JSONUtils.mayBeJSON(entityContent)) {
            log.error("The response data is not json format, data[{}]", entityContent);
            throw new OAuthSdkException("response data is not json");
        }

        return JSONObject.fromObject(entityContent);
    }

    /**
     * validate if success response
     *
     * @param json
     * @return
     */
    private boolean isSuccessResult(JSONObject json) {
        // never be null
        return (null != json && "ok".equalsIgnoreCase(json.optString("result", StringUtils.EMPTY)));
    }

}
