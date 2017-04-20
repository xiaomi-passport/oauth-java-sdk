# 小米帐号开放平台OAuth JAVA SDK使用说明

------

### 小米OAuth简介
http://dev.xiaomi.com/docs/passport/oauth2/

### 小米帐号开放平台文档
http://dev.xiaomi.com/docs/passport/user-guide/

### JAVA SDK说明
> * com.xiaomi.api.httpXMHttpClient -- 基础HTTP请求封装
> * com.xiaomi.api.XMOAuthHttpClient -- 针对OAuth授权流程相关HTTP请求封装
> * com.xiaomi.api.httpXMApiHttpClient -- 针对API请求相关HTTP请求封装

### DEMO
#### 1. 获取授权URL DEMO

```java
public static void main(String[] args) {
    long cid = 179887661252608l;
    String cs = "";
    String uri = "http://xiaomi.com";

    XMHttpClient xmHttpClient = new com.xiaomi.api.http.XMHttpClient();
    XMOAuthHttpClient hc = new XMOAuthHttpClient(cid, cs, uri, hc);
    String url = xmoAuthHttpClient.getAuthorizeUrl();
}
```

复制授权url到浏览器, 输入用户名密码, 浏览器跳转到`http://xiaomi.com?code=code-type`
复制code-value作为步骤2的输入。

#### 2.  获取accessToken DEMO

```java
public static void main(String[] args) throws XMException, URISyntaxException {
    long cid = 179887661252608l;
    String cs = "xxxxx";
    String uri = "http://xiaomi.com";
    String codeValue = "code-type";

    XMHttpClient hc = new com.xiaomi.api.http.XMHttpClient();
    XMOAuthHttpClient xmoAuthHttpClient = new XMOAuthHttpClient(cid, cs, uri, hc);
    AccessToken token = xmoAuthHttpClient.getAccessTokenByAuthorizationCode(codeValue);
}
```

AccessToken包括如下信息:

```java
protected String token;
protected String refreshToken;
protected String scope;
protected long expiresIn;
protected String tokenType;
protected String macKey;
protected String macAlgorithm;
```

#### 3. 通过refreshToken换取accessToken

```java
public static void main(String[] args) throws XMException, URISyntaxException {
    long cId = 179887661252608l;
    String cs = "xxxxx";
    String uri = "http://xiaomi.com";
    String refreshToken = "code-type";

    XMHttpClient hc = new com.xiaomi.api.http.XMHttpClient();
    XMOAuthHttpClient xmoAuthHttpClient = new XMOAuthHttpClient(cId, cs, uri, hc);
    AccessToken token = xmoAuthHttpClient.getAccessTokenByRefreshToken(refreshToken);
}
```

#### 3. 访问OPEN API DEMO (以获取用户名片为例)
```java
public static void main(String[] args) throws Exception {
    long cId = 179887661252608l;
    String tokenId = "accessToken.token";
    List<Header> headers = new ArrayList<Header>();

    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("clientId", String.valueOf(cId)));
    params.add(new BasicNameValuePair("token", tokenId));

    String macKey = "accessToken.macKey";

    String nonce = XMUtil.generateNonce();
    String qs = URLEncodedUtils.format(params, "UTF-8");
    String apiHost  "open.account.xiaomi.com";
    String apiPath = "/user/profile";
    String mac = XMUtil.getMacAccessTokenSignatureString(nonce, "GET", apiHost,apiPath, 
                                                          qs,macKey, "HmacSHA1");
    Header macHeader = XMUtil.buildMacRequestHead(tokenId, nonce, mac);

    headers.add(macHeader);

    XMApiHttpClient client = new XMApiHttpClient(cId, tokenId, new XMHttpClient());
    JSONObject json = client.apiCall("/user/profile", params, headers, "GET");
    System.out.println(json.toString());
}
```
