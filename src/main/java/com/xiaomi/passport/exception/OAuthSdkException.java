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
package com.xiaomi.passport.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * user define exception
 *
 * @author zhenchao.wang 2017-04-14 13:53:00
 * @version 1.0.0
 */
public class OAuthSdkException extends Exception {

    private static final long serialVersionUID = 1675452787643768680L;

    private int httpStatus = -1;

    private int errorCode = -1;

    /** error description */
    private String desc = StringUtils.EMPTY;

    public OAuthSdkException() {
        super();
    }

    public OAuthSdkException(String message) {
        super(message);
    }

    public OAuthSdkException(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuthSdkException(Throwable cause) {
        super(cause);
    }

    public OAuthSdkException(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public OAuthSdkException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public OAuthSdkException(String message, Throwable cause, int httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public OAuthSdkException(Throwable cause, int httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public OAuthSdkException(int errorCode, String desc) {
        this.errorCode = errorCode;
        this.desc = desc;
    }

    public OAuthSdkException(String message, int errorCode, String desc) {
        super(message);
        this.errorCode = errorCode;
        this.desc = desc;
    }

    public OAuthSdkException(String message, Throwable cause, int errorCode, String desc) {
        super(message, cause);
        this.errorCode = errorCode;
        this.desc = desc;
    }

    public OAuthSdkException(Throwable cause, int errorCode, String desc) {
        super(cause);
        this.errorCode = errorCode;
        this.desc = desc;
    }

    public OAuthSdkException(int errorCode, int httpStatus, String desc) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.desc = desc;
    }

    public OAuthSdkException(String message, int errorCode, int httpStatus, String desc) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.desc = desc;
    }

    public OAuthSdkException(String message, Throwable cause, int errorCode, int httpStatus, String desc) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.desc = desc;
    }

    public OAuthSdkException(Throwable cause, int errorCode, int httpStatus, String desc) {
        super(cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.desc = desc;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getDesc() {
        return desc;
    }
}
