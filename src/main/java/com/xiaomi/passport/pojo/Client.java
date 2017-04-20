package com.xiaomi.passport.pojo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * client pojo
 *
 * @author zhenchao.wang 2017-04-20 16:35
 * @version 1.0.0
 */
public class Client {

    private Long id;

    private String name;

    private String key;

    private String secret;

    private String redirectUri;

    private Integer[] scopes;

    private String packageName;

    public Client() {
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public Long getId() {
        return id;
    }

    public Client setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Client setName(String name) {
        this.name = name;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Client setKey(String key) {
        this.key = key;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public Client setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public Client setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public Integer[] getScopes() {
        return scopes;
    }

    public Client setScopes(Integer[] scopes) {
        this.scopes = scopes;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public Client setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }
}
