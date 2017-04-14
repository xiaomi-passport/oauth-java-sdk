package com.xiaomi.passport.constant;

/**
 * global constant
 *
 * @author zhenchao.wang 2017-04-14 13:46
 * @version 1.0.0
 */
public interface GlobalConstants {

    /** request path */

    String OAUTH2_HOST = "https://account.xiaomi.com";

    String AUTHORIZE_PATH = "/oauth2/authorize";

    String TOKEN_PATH = "/oauth2/token";

    /** parameter name */

    String CLIENT_ID = "client_id";

    String TOKEN_TYPE = "token_type";

    String SCOPE = "scope";

    String ACCESS_TOKEN = "access_token";

    String MAC_KEY = "mac_key";

    String MAC_ALGORITHM = "mac_algorithm";

    String EXPIRES_IN = "expires_in";

    String REFRESH_TOKEN = "refresh_token";

    /** others */

    String HMAC_SHA1 = "HmacSHA1";

    String DEFAULT_CHARSET = "UTF-8";

    String JSON_SAFE_FLAG = "&&&START&&&";

}
