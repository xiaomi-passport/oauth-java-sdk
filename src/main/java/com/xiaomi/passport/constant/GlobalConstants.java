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

    String STATE = "state";

    String ACCESS_TOKEN = "access_token";

    String MAC_KEY = "mac_key";

    String MAC_ALGORITHM = "mac_algorithm";

    String EXPIRES_IN = "expires_in";

    String REFRESH_TOKEN = "refresh_token";

    String RESPONSE_TYPE = "response_type";

    String REDIRECT_URI = "redirect_uri";

    String GRANT_TYPE = "grant_type";

    String CLIENT_SECRET = "client_secret";

    String CODE = "code";

    /** others */

    String HMAC_SHA1 = "HmacSHA1";

    String DEFAULT_CHARSET = "UTF-8";

    String JSON_SAFE_FLAG = "&&&START&&&";

}
