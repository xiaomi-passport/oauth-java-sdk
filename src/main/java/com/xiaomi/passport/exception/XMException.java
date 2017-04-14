/***************************************************************************
 *
 * Copyright (c) 2013 xiaomi.com, Inc. All Rights Reserved
 *
 **************************************************************************/

package com.xiaomi.passport.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * user define exception
 *
 * @author zhenchao.wang 2017-04-14 13:53:00
 * @version 1.0.0
 */
public class XMException extends Exception {

    private static final long serialVersionUID = 1675452787643768680L;

    private int errorCode = -1;

    private int httpStatus = -1;

    /** error description */
    private String desc = StringUtils.EMPTY;

    public XMException() {
        super();
    }

    public XMException(String message) {
        super(message);
    }

    public XMException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMException(Throwable cause) {
        super(cause);
    }

    public XMException(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public XMException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public XMException(String message, Throwable cause, int httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public XMException(Throwable cause, int httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public XMException(int errorCode, String desc) {
        this.errorCode = errorCode;
        this.desc = desc;
    }

    public XMException(String message, int errorCode, String desc) {
        super(message);
        this.errorCode = errorCode;
        this.desc = desc;
    }

    public XMException(String message, Throwable cause, int errorCode, String desc) {
        super(message, cause);
        this.errorCode = errorCode;
        this.desc = desc;
    }

    public XMException(Throwable cause, int errorCode, String desc) {
        super(cause);
        this.errorCode = errorCode;
        this.desc = desc;
    }

    public XMException(int errorCode, int httpStatus, String desc) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.desc = desc;
    }

    public XMException(String message, int errorCode, int httpStatus, String desc) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.desc = desc;
    }

    public XMException(String message, Throwable cause, int errorCode, int httpStatus, String desc) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.desc = desc;
    }

    public XMException(Throwable cause, int errorCode, int httpStatus, String desc) {
        super(cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.desc = desc;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getDesc() {
        return desc;
    }
}
