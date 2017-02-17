/***************************************************************************
 *
 * Copyright (c) 2013 xiaomi.com, Inc. All Rights Reserved
 *
 **************************************************************************/

package com.xiaomi.api.http;

import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class XMApiHttpClient {

    static Logger log = Logger.getLogger(XMApiHttpClient.class.getName());

    public static final String API_HOST = "https://open.account.xiaomi.com";

    protected String accessTokenId;

    protected long clientId;

    protected XMHttpClient xmHttpClient;

    public XMApiHttpClient(long clientId, String accessTokenId, XMHttpClient xmHttpClient) {
        super();
        this.clientId = clientId;
        this.accessTokenId = accessTokenId;
        this.xmHttpClient = xmHttpClient;
    }

    /**
     * @param path api url的path部分，例如：/user/profile
     * @param params NameValuePair list
     * @param method GET/POST
     * @return
     * @throws XMException
     * @throws URISyntaxException
     * @throws JSONException
     */
    public JSONObject apiCall(String path,
                              List<NameValuePair> params,
                              String method) throws XMException, URISyntaxException, JSONException {
        return apiCall(path, params, null, method);
    }

    /**
     * @param path api url的path部分，例如：/user/profile
     * @param params NameValuePair list
     * @param headers Header list
     * @param method GET/POST
     * @return
     * @throws XMException
     * @throws URISyntaxException
     * @throws JSONException
     */
    public JSONObject apiCall(String path,
                              List<NameValuePair> params,
                              List<Header> headers,
                              String method) throws XMException, URISyntaxException, JSONException {
        JSONObject jsonResult = null;

        if (null == params) {
            params = new ArrayList<NameValuePair>();
        }
        if (null == headers) {
            headers = new ArrayList<Header>();
        }
        if (StringUtils.equalsIgnoreCase(method, XMHttpClient.METHOD_GET)) {
            String result = xmHttpClient.get(API_HOST + path, params, headers);
            jsonResult = new JSONObject(result);
        } else if (StringUtils.equalsIgnoreCase(method, XMHttpClient.METHOD_POST)) {
            String result = xmHttpClient.post(API_HOST + path, params, headers);
            jsonResult = new JSONObject(result);
        }
        return jsonResult;
    }

    public String getAccessTokenId() {
        return accessTokenId;
    }

    public void setAccessTokenId(String accessTokenId) {
        this.accessTokenId = accessTokenId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }
}
