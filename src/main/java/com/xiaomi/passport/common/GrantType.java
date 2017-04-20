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

/**
 * oauth 2.0 grant type
 *
 * @author zhenchao.wang 2017-04-20 17:31
 * @version 1.0.0
 */
public enum GrantType {

    AUTHORIZATION_CODE("authorization_code"),

    PASSWORD_CREDENTIALS("password"),

    CLIENT_CREDENTIALS("client_credentials"),

    REFRESH_TOKEN("refresh_token");

    private String type;

    GrantType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * tell whether {@param grantType} is valid
     *
     * @param grantType
     * @return
     */
    public static boolean isValid(String grantType) {
        return AUTHORIZATION_CODE.getType().equals(grantType)
                || PASSWORD_CREDENTIALS.getType().equals(grantType) || CLIENT_CREDENTIALS.getType().equals(grantType);
    }

    /**
     * change string param to {@code GrantType} object
     *
     * @param grantType
     * @return
     */
    public static GrantType toGrantType(String grantType) {
        for (final GrantType type : GrantType.values()) {
            if (type.getType().equals(grantType)) {
                return type;
            }
        }
        return null;
    }
}