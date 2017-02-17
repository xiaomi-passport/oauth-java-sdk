/***************************************************************************
 *
 * Copyright (c) 2013 xiaomi.com, Inc. All Rights Reserved
 *
 **************************************************************************/

package com.xiaomi.api.http;

import com.xiaomi.utils.XMUtil;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class XMHttpClient {

    static Logger log = Logger.getLogger(XMHttpClient.class.getName());

    public static final String METHOD_GET = "GET";

    public static final String METHOD_POST = "POST";

    public static final int CONNECT_TIMEOUT = 30 * 1000;

    public static final int SOCKET_TIMEOUT = 30 * 1000;

    public static final int MAX_CON_PER_HOST = 100;

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String HEADER_FLAG = "&&&START&&&";

    private HttpClient client = null;

    private MultiThreadedHttpConnectionManager connectionManager;

    public XMHttpClient() {
        this(MAX_CON_PER_HOST, CONNECT_TIMEOUT, SOCKET_TIMEOUT);
    }

    public XMHttpClient(int maxConPerHost, int conTimeOutMs, int soTimeOutMs) {
        connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = connectionManager.getParams();
        params.setDefaultMaxConnectionsPerHost(maxConPerHost);
        params.setConnectionTimeout(conTimeOutMs);
        params.setSoTimeout(soTimeOutMs);

        HttpClientParams clientParams = new HttpClientParams();
        clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        client = new org.apache.commons.httpclient.HttpClient(clientParams, connectionManager);
        Protocol https = new Protocol("https", new XMSSLSocketFactory(), 443);
        Protocol.registerProtocol("https", https);
    }

    public String get(String url) throws XMException, URISyntaxException {
        return get(url, new ArrayList<NameValuePair>(), new ArrayList<Header>());
    }

    public String get(String url, List<NameValuePair> params) throws XMException, URISyntaxException {
        return get(url, params, new ArrayList<Header>());
    }

    public String get(String url, List<NameValuePair> params, List<Header> headers) throws XMException, URISyntaxException {
        XMUtil.Log(log, "Request: GET:" + url);
        URIBuilder uriBuilder = new URIBuilder(url);
        if (null != params && !params.isEmpty()) {
            String qs = URLEncodedUtils.format(params, DEFAULT_CHARSET);
            uriBuilder.setQuery(qs);
        }
        GetMethod method = new GetMethod(uriBuilder.toString());
        return httpRequest(method, headers);
    }

    public String post(String url) throws XMException, URISyntaxException {
        return post(url, new ArrayList<NameValuePair>(), new ArrayList<Header>());
    }

    public String post(String url, List<NameValuePair> params) throws XMException, URISyntaxException {
        return post(url, params, new ArrayList<Header>());
    }

    public String post(String url, List<NameValuePair> params, List<Header> headers) throws XMException, URISyntaxException {
        XMUtil.Log(log, "Request: POST:" + url);
        URIBuilder uriBuilder = new URIBuilder(url);
        if (!params.isEmpty()) {
            String qs = URLEncodedUtils.format(params, DEFAULT_CHARSET);
            uriBuilder.setQuery(qs);
        }
        PostMethod method = new PostMethod(uriBuilder.toString());
        return httpRequest(method, headers);
    }

    protected String httpRequest(HttpMethod method, List<Header> headers) throws XMException {
        int responseCode = -1;
        try {
            if (null != headers && !headers.isEmpty()) {
                client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
            }
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(2, false));
            client.executeMethod(method);
            responseCode = method.getStatusCode();
            XMUtil.Log(log, "https StatusCode:" + String.valueOf(responseCode));
            String response = method.getResponseBodyAsString();
            // 301 => "HTTP/1.1 301 Moved Permanently",
            // 302 => "HTTP/1.1 302 Found",
            // 303 => "HTTP/1.1 303 See Other",
            // 307 => "HTTP/1.1 307 Temporary Redirect",
            if (responseCode != 200 && responseCode != 301 && responseCode != 302 && responseCode != 303 && responseCode != 307) {
                throw new XMException(responseCode, method.getStatusCode());
            }
            // 替换头部的&&&START&&&
            response = response.replace(HEADER_FLAG, "");
            return response;
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
            throw new XMException(ioe.getMessage(), ioe, responseCode);
        } finally {
            method.releaseConnection();
        }
    }

    private static class XMSSLSocketFactory implements ProtocolSocketFactory {
        private SSLContext sslcontext = null;

        private SSLContext createSSLContext() {
            SSLContext sslcontext = null;
            try {
                sslcontext = SSLContext.getInstance("SSL");
                sslcontext.init(null, new TrustManager[] {
                        new EasyX509TrustManager()
                }, new java.security.SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                log.error(e.getMessage());
            } catch (KeyManagementException e) {
                log.error(e.getMessage());
            }
            return sslcontext;
        }

        private SSLContext getSSLContext() {
            if (this.sslcontext == null) {
                this.sslcontext = createSSLContext();
            }
            return this.sslcontext;
        }

        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            return getSSLContext().getSocketFactory().createSocket(host, port);
        }

        public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
            return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
        }

        public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params)
                throws IOException {
            if (params == null) {
                throw new IllegalArgumentException("Parameters may not be null");
            }
            int timeout = params.getConnectionTimeout();
            SocketFactory socketfactory = getSSLContext().getSocketFactory();
            if (timeout == 0) {
                return socketfactory.createSocket(host, port, localAddress, localPort);
            } else {
                Socket socket = socketfactory.createSocket();
                SocketAddress localAddr = new InetSocketAddress(localAddress, localPort);
                SocketAddress remoteAddr = new InetSocketAddress(host, port);
                socket.bind(localAddr);
                socket.connect(remoteAddr, timeout);
                return socket;
            }
        }

        private static class EasyX509TrustManager implements X509TrustManager {

            private static Pattern cnPattern = Pattern.compile("(?i)(cn=)([^,]*)");

            class configuration {
                static final boolean isVerifyChainEnabled = true;
                static final boolean isExpiredCertificatesCheckEnabled = true;
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] x509Certificates, String arg1) throws CertificateException {
                int nSize = x509Certificates.length;

                List<String> peerIdentities = getPeerIdentity(x509Certificates[0]);

                if (configuration.isVerifyChainEnabled) {
                    Principal principalLast = null;
                    for (int i = nSize - 1; i >= 0; i--) {
                        X509Certificate x509certificate = x509Certificates[i];
                        Principal principalIssuer = x509certificate.getIssuerDN();
                        Principal principalSubject = x509certificate.getSubjectDN();
                        if (principalLast != null) {
                            if (principalIssuer.equals(principalLast)) {
                                try {
                                    PublicKey publickey = x509Certificates[i + 1].getPublicKey();
                                    x509Certificates[i].verify(publickey);
                                } catch (GeneralSecurityException generalsecurityexception) {
                                    throw new CertificateException("signature verification failed of " + peerIdentities);
                                }
                            } else {
                                throw new CertificateException("subject/issuer verification failed of " + peerIdentities);
                            }
                        }
                        principalLast = principalSubject;
                    }
                }
                if (configuration.isExpiredCertificatesCheckEnabled) {
                    Date date = new Date();
                    for (int i = 0; i < nSize; i++) {
                        try {
                            x509Certificates[i].checkValidity(date);
                        } catch (GeneralSecurityException generalsecurityexception) {
                            throw new CertificateException("Expired Certificates.");
                        }
                    }
                }
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }

            public static List<String> getPeerIdentity(X509Certificate x509Certificate) {
                List<String> names = getSubjectAlternativeNames(x509Certificate);
                if (names.isEmpty()) {
                    String name = x509Certificate.getSubjectDN().getName();
                    Matcher matcher = cnPattern.matcher(name);
                    if (matcher.find()) {
                        name = matcher.group(2);
                    }
                    names = new ArrayList<String>();
                    names.add(name);
                }
                return names;
            }

            private static List<String> getSubjectAlternativeNames(X509Certificate certificate) {
                List<String> identities = new ArrayList<String>();
                try {
                    Collection<List<?>> altNames = certificate.getSubjectAlternativeNames();
                    if (altNames == null) {
                        return Collections.emptyList();
                    }
                } catch (CertificateParsingException e) {
                    e.printStackTrace();
                }
                return identities;
            }
        }
    }
}
