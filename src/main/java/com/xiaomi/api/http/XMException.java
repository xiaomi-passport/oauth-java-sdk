/***************************************************************************
 *
 * Copyright (c) 2013 xiaomi.com, Inc. All Rights Reserved
 *
 **************************************************************************/
package com.xiaomi.api.http;

import org.json.JSONException;
import org.json.JSONObject;

public class XMException extends Exception {
    private int statusCode = -1;
    private int error = -1;
    private String request;
    private String errorDescription;
    private static final long serialVersionUID = -2623309261327598087L;

    public XMException(String msg) {
        super(msg);
    }

    public XMException(Exception cause) {
        super(cause);
    }
    
    public XMException(int httpCode , int statusCode) {
        super("http code: " + httpCode + "\nstatusCode: " + statusCode);
        this.statusCode = statusCode;
    }
    
    public XMException(JSONObject json) throws JSONException {
        super("error:" + json.getString("error_description") + " error_code:" + json.getInt("error"));
        this.error = json.getInt("error");
        this.errorDescription = json.getString("error_description");
    }
    
    public XMException(int httpCode, JSONObject json, int statusCode) throws JSONException {
        super("http code: " + httpCode + "\n error:" + json.getString("error_description") + " error_code:" + json.getInt("error"));
        this.statusCode = statusCode;
        this.error = json.getInt("error");
        this.errorDescription = json.getString("error_description");
    }

    public XMException(String msg, Exception cause) {
        super(msg, cause);
    }

    public XMException(String msg, Exception cause, int statusCode) {
        super(msg, cause);
        this.statusCode = statusCode;

    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public int getError() {
        return error;
    }

    public String getRequest() {
        return request;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
    
}
