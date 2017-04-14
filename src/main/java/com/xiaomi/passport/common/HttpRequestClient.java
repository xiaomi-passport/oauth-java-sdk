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
package com.xiaomi.passport.common;

import com.xiaomi.passport.constant.GlobalConstants;
import com.xiaomi.passport.exception.OAuthSdkException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * http common request client
 *
 * @author zhenchao.wang 2017-04-14 16:24:51
 * @version 1.0.0
 */
public class HttpRequestClient {

    private final Logger log = LoggerFactory.getLogger(HttpRequestClient.class);

    public static final int CONNECT_TIMEOUT = 30 * 1000;

    public static final int SOCKET_TIMEOUT = 30 * 1000;

    public static final int MAX_CON_PER_HOST = 100;

    private HttpClient httpClient = null;

    public HttpRequestClient() {
        this(MAX_CON_PER_HOST, CONNECT_TIMEOUT, SOCKET_TIMEOUT);
    }

    public HttpRequestClient(int maxConnection, int connectTimeout, int socketTimeout) {
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", SSLConnectionSocketFactory.getSocketFactory())
                        .build());
        // set max connection
        connectionManager.setMaxTotal(maxConnection);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .build();

        httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .build();
    }

    /**
     * get request
     *
     * @param url
     * @return
     * @throws OAuthSdkException
     * @throws URISyntaxException
     */
    public String get(String url) throws OAuthSdkException, URISyntaxException {
        return this.get(url, new ArrayList<NameValuePair>(), null);
    }

    /**
     * get request
     *
     * @param url
     * @param params
     * @return
     * @throws OAuthSdkException
     * @throws URISyntaxException
     */
    public String get(String url, List<NameValuePair> params) throws OAuthSdkException, URISyntaxException {
        return this.get(url, params, null);
    }

    /**
     * get request
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws OAuthSdkException
     * @throws URISyntaxException
     */
    public String get(String url, List<NameValuePair> params, Header[] headers) throws OAuthSdkException, URISyntaxException {
        log.info("Get request url[{}]", url);
        URIBuilder builder = new URIBuilder(url);
        if (CollectionUtils.isNotEmpty(params)) {
            builder.setParameters(params);
        }
        return this.request(new HttpGet(builder.build()), headers);
    }

    /**
     * post request
     *
     * @param url
     * @return
     * @throws OAuthSdkException
     * @throws URISyntaxException
     */
    public String post(String url) throws OAuthSdkException, URISyntaxException {
        return this.post(url, new ArrayList<NameValuePair>(), null);
    }

    /**
     * post request
     *
     * @param url
     * @param params
     * @return
     * @throws OAuthSdkException
     * @throws URISyntaxException
     */
    public String post(String url, List<NameValuePair> params) throws OAuthSdkException, URISyntaxException {
        return this.post(url, params, null);
    }

    /**
     * post request
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws OAuthSdkException
     * @throws URISyntaxException
     */
    public String post(String url, List<NameValuePair> params, Header[] headers) throws OAuthSdkException, URISyntaxException {
        log.info("Post request url[{}]", url);
        URIBuilder builder = new URIBuilder(url);
        if (CollectionUtils.isNotEmpty(params)) {
            builder.setParameters(params);
        }
        return request(new HttpPost(builder.build()), headers);
    }

    /**
     * execute the specify request
     *
     * @param request
     * @param headers
     * @return
     * @throws OAuthSdkException
     */
    protected String request(HttpUriRequest request, Header[] headers) throws OAuthSdkException {
        try {
            if (ArrayUtils.isNotEmpty(headers)) {
                request.setHeaders(headers);
            }
            HttpResponse response = httpClient.execute(request);
            StatusLine statusLine = response.getStatusLine();
            String responseString = response.toString();
            if (!this.isExpectedHttpStatus(statusLine.getStatusCode())) {
                log.error("The http status [{}] is not expected, !", statusLine.getStatusCode());
                throw new OAuthSdkException(responseString, statusLine.getStatusCode());
            }
            return responseString.replace(GlobalConstants.JSON_SAFE_FLAG, StringUtils.EMPTY);
        } catch (IOException e) {
            log.error("Execute http request error, uri[{}]!", request.getURI(), e);
            throw new OAuthSdkException(e.getMessage(), e);
        }
    }

    /**
     * validate if is expected http status
     *
     * 301 => "HTTP/1.1 301 Moved Permanently",
     * 302 => "HTTP/1.1 302 Found",
     * 303 => "HTTP/1.1 303 See Other",
     * 307 => "HTTP/1.1 307 Temporary Redirect",
     *
     * @param status
     * @return
     */
    private boolean isExpectedHttpStatus(int status) {
        switch (status) {
            case 200:
            case 301:
            case 302:
            case 303:
            case 307:
                return true;
            default:
                return false;
        }
    }

}
