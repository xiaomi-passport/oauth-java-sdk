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
package com.xiaomi.passport.utils;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.xiaomi.passport.constant.GlobalConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * sdk common utils
 *
 * @author zhenchao.wang 2017-04-14 14:41:54
 * @version 1.0.0
 */
public class CommonUtils {

    private static final Logger log = LoggerFactory.getLogger(CommonUtils.class);

    private static Random random = new SecureRandom();

    private CommonUtils() {
    }

    /**
     * generate mac signature
     * format : nonce + \n + method(POST\GET) + \n + host + \n + uriPath + \n + qs + \n
     *
     * @param nonce random string
     * @param method POST or GET
     * @param uriPath request url path
     * @param qs query param
     * @param macAlgorithm signature algorithm, must be HmacSHA1
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String getMacAccessTokenSignature(
            String nonce, String method, String host, String uriPath, String qs, String macKey, String macAlgorithm)
            throws InvalidKeyException, NoSuchAlgorithmException {

        log.debug("Get mac access token signature: nonce[{}], method[{}], host[{}], uriPath[{}], qs[{}], macKey[{}], macAlgorithm[{}].",
                nonce, method, host, uriPath, qs, macKey, macAlgorithm);

        List<String> elements = new ArrayList<String>();
        elements.add(nonce);
        elements.add(method.toUpperCase());
        elements.add(host);
        elements.add(uriPath);

        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotBlank(qs)) {
            // qs may be blank
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            URLEncodedUtils.parse(params, new Scanner(qs), GlobalConstants.DEFAULT_CHARSET);
            Collections.sort(params, new Comparator<NameValuePair>() {
                public int compare(NameValuePair p1, NameValuePair p2) {
                    return p1.getName().compareTo(p2.getName());
                }
            });
            builder.append(URLEncodedUtils.format(params, GlobalConstants.DEFAULT_CHARSET));
        }
        elements.add(builder.toString());

        String sign = StringUtils.join(elements.toArray(), "\n");
        sign += "\n";  // don't forget this
        log.debug("The generate max sign is [{}]", sign);

        if (GlobalConstants.HMAC_SHA1.equalsIgnoreCase(macAlgorithm)) {
            try {
                return Base64.encode(
                        encryptHmacSha1(
                                sign.getBytes(GlobalConstants.DEFAULT_CHARSET), macKey.getBytes(GlobalConstants.DEFAULT_CHARSET)));
            } catch (UnsupportedEncodingException e) {
                // never happen
            }
        } else {
            throw new NoSuchAlgorithmException("Unsupported mac algorithm : " + macAlgorithm);
        }
        return StringUtils.EMPTY;
    }

    /**
     * build mac request header content
     *
     * @param accessTokenId
     * @param nonce
     * @param mac
     * @return
     */
    public static Header buildMacRequestHeader(String accessTokenId, String nonce, String mac) {
        String content = "MAC access_token=\"%s\", nonce=\"%s\",mac=\"%s\"";
        try {
            content = String.format(content,
                    URLEncoder.encode(accessTokenId, GlobalConstants.DEFAULT_CHARSET),
                    URLEncoder.encode(nonce, GlobalConstants.DEFAULT_CHARSET),
                    URLEncoder.encode(mac, GlobalConstants.DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            // never happen
        }
        return new BasicHeader("Authorization", content);
    }

    /**
     * generate nonce
     *
     * @return
     */
    public static String generateNonce() {
        return String.format("%d:%d", random.nextLong(), System.currentTimeMillis() / (1000 * 60));
    }

    /**
     * encrypt by HmacSha1
     *
     * @param data
     * @param key
     * @return return null if encrypt failed
     * @throws InvalidKeyException
     */
    public static byte[] encryptHmacSha1(byte[] data, byte[] key) throws InvalidKeyException {
        try {
            Mac mac = Mac.getInstance(GlobalConstants.HMAC_SHA1);
            mac.init(new SecretKeySpec(key, GlobalConstants.HMAC_SHA1));
            mac.update(data);
            return mac.doFinal();
        } catch (NoSuchAlgorithmException e) {
            // never happen
        }
        return null;
    }
}
