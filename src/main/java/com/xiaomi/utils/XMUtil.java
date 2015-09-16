/***************************************************************************
 *
 * Copyright (c) 2013 xiaomi.com, Inc. All Rights Reserved
 *
 **************************************************************************/
package com.xiaomi.utils;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;

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

public class XMUtil {
    public static boolean DEBUG = true;
    public static Logger log = Logger.getLogger(XMUtil.class.getName());

    private static final String HMAC_SHA1 = "HmacSHA1";
    private static Random random = new SecureRandom();

    public static void Log(Logger log, String content) {
        if (DEBUG) {
            log.debug(content);
        }
    }

    /**
     * 签名字符串的格式 nonce + \n + method(POST\GET) + \n + host + \n + uriPaht + \n + req(query
     * param按照key的字典序)+\n(最后也需要添加一个\n)
     * 
     * @param nonce 随机字符串
     * @param method POST GET\
     * @param uriPath requet的path部分
     * @param req query param按照key的字典序
     * @param macAlgorithm 签名算法 必须是 HmacSHA1
     * @return 需要签名的字符串
     * @throws UnsupportedEncodingException
     * @throws 3NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String getMacAccessTokenSignatureString(String nonce, 
                                                          String method, 
                                                          String host, 
                                                          String uriPath, 
                                                          String qs,
                                                          String macKey, 
                                                          String macAlgorithm) throws InvalidKeyException, 
                                                                                    NoSuchAlgorithmException, 
                                                                                    UnsupportedEncodingException {
        List<String> exps = new ArrayList<String>();
        Log(log, String.format("mac sign macKey %s macAlgorithm %s", macKey, macAlgorithm));
        exps.add(nonce);
        Log(log, String.format("mac sign nonce %s", nonce));
        exps.add(method.toUpperCase());
        Log(log, String.format("mac sign method %s", method.toUpperCase()));
        exps.add(host);
        Log(log, String.format("mac sign host %s", host));

        exps.add(uriPath);
        Log(log, String.format("mac sign uriPath %s", uriPath));

        // qs可以为空
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotEmpty(qs)) {
            Log(log, String.format("mac sign qs %s", qs));
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            URLEncodedUtils.parse(paramList, new Scanner(qs), "UTF-8");
            Collections.sort(paramList, new Comparator<NameValuePair>() {
                public int compare(NameValuePair p1, NameValuePair p2) {
                    return p1.getName().compareTo(p2.getName());
                }
            });
            sb.append(URLEncodedUtils.format(paramList,  "UTF-8"));
        }
        exps.add(sb.toString());
        String joined = StringUtils.join(exps.toArray(), "\n");
        // 补上最后一个\n
        joined += "\n";
        Log(log, String.format("Mac sign string %s", joined));
        byte[] signatureBytes = null;
        if (StringUtils.equalsIgnoreCase(macAlgorithm, "HmacSHA1")) {
            signatureBytes = encryptHMACSha1(joined.getBytes("UTF-8"), macKey.getBytes("UTF-8"));
        } else {
            throw new NoSuchAlgorithmException("error mac algorithm : " + macAlgorithm);
        }
        String signature = encodeSign(signatureBytes);
        return signature;
    }

    /**
     * @param accessTokenId
     * @param nonce
     * @param mac
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Header buildMacRequestHead(String accessTokenId, 
                                             String nonce, 
                                             String mac) throws UnsupportedEncodingException {
        String headContent = "MAC access_token=\"%s\", nonce=\"%s\",mac=\"%s\"";
        headContent = String.format(headContent, URLEncoder.encode(accessTokenId, "utf-8"),
                                    URLEncoder.encode(nonce, "utf-8"), URLEncoder.encode(mac, "utf-8"));
        Header header = new Header("Authorization", headContent);
        return header;
    }

    /**
     * 获取随机nonce值 64是随机数，后面32位是总的分钟数
     * 
     * @return
     */
    public static String generateNonce() {
        long n = random.nextLong();
        int m = (int) (System.currentTimeMillis() / (1000 * 60));
        return n + ":" + m;
    }

    public static String encodeSign(byte[] key) {
        try {
            return new String(Base64.encodeBase64(key), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * HMAC-SHA1
     * 
     * @param data
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static byte[] encryptHMACSha1(byte[] data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(signingKey);
        mac.update(data);
        return mac.doFinal();
    }
}
