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

import com.xiaomi.passport.exception.OAuthSdkException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
 * oauth http request client
 *
 * @author zhenchao.wang 2017-04-14 16:24:51
 * @version 1.0.0
 */
public class OAuthHttpClient {

    private final Logger log = LoggerFactory.getLogger(OAuthHttpClient.class);

    private static final int CONNECT_TIMEOUT = 30 * 1000;

    private static final int SOCKET_TIMEOUT = 30 * 1000;

    private static final int MAX_CON_PER_HOST = 100;

    private HttpClient httpClient = null;

    public OAuthHttpClient() {
        this(MAX_CON_PER_HOST, CONNECT_TIMEOUT, SOCKET_TIMEOUT);
    }

    public OAuthHttpClient(int maxConnection, int connectTimeout, int socketTimeout) {
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
     * get request without params
     *
     * @param url
     * @return
     * @throws OAuthSdkException
     */
    public HttpResponse get(String url) throws OAuthSdkException {
        return this.get(url, new ArrayList<NameValuePair>(), null);
    }

    /**
     * get request with params
     *
     * @param url
     * @param params
     * @return
     * @throws OAuthSdkException
     */
    public HttpResponse get(String url, List<NameValuePair> params) throws OAuthSdkException {
        return this.get(url, params, null);
    }

    /**
     * get request with params and headers
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws OAuthSdkException
     */
    public HttpResponse get(String url, List<NameValuePair> params, Header[] headers) throws OAuthSdkException {
        try {
            URIBuilder builder = new URIBuilder(url);
            if (CollectionUtils.isNotEmpty(params)) {
                builder.setParameters(params);
            }
            HttpUriRequest request = new HttpGet(builder.build());
            if (ArrayUtils.isNotEmpty(headers)) {
                request.setHeaders(headers);
            }
            return httpClient.execute(request);
        } catch (URISyntaxException e) {
            log.error("The uri[{}] is illegal!", url, e);
            throw new OAuthSdkException("url syntax exception", e);
        } catch (IOException e) {
            log.error("Execute get request url[{}] error!", url, e);
            throw new OAuthSdkException("execute get request error", e);
        }
    }

    /**
     * post request without params
     *
     * @param url
     * @return
     * @throws OAuthSdkException
     */
    public HttpResponse post(String url) throws OAuthSdkException {
        return this.post(url, new ArrayList<NameValuePair>(), null);
    }

    /**
     * post request with params
     *
     * @param url
     * @param params
     * @return
     * @throws OAuthSdkException
     */
    public HttpResponse post(String url, List<NameValuePair> params) throws OAuthSdkException {
        return this.post(url, params, null);
    }

    /**
     * post request with params and headers
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws OAuthSdkException
     */
    public HttpResponse post(String url, List<NameValuePair> params, Header[] headers) throws OAuthSdkException {
        try {
            URIBuilder builder = new URIBuilder(url);
            if (CollectionUtils.isNotEmpty(params)) {
                builder.setParameters(params);
            }
            HttpUriRequest request = new HttpPost(builder.build());
            if (ArrayUtils.isNotEmpty(headers)) {
                request.setHeaders(headers);
            }
            return httpClient.execute(request);
        } catch (URISyntaxException e) {
            log.error("The uri[{}] is illegal!", url, e);
            throw new OAuthSdkException("url syntax exception", e);
        } catch (IOException e) {
            log.error("Execute post request url[{}] error!", url, e);
            throw new OAuthSdkException("execute post request error", e);
        }
    }

}
