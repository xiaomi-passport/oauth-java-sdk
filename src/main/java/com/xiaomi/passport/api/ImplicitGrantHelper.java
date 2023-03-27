package com.xiaomi.passport.api;

import com.xiaomi.passport.common.OAuthHttpClient;
import com.xiaomi.passport.common.ResponseType;
import com.xiaomi.passport.constant.GlobalConstants;
import com.xiaomi.passport.pojo.AccessToken;
import com.xiaomi.passport.pojo.Client;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * implicit grant type
 * deprecated in 2023-3-27
 *
 * @author zhenchao.wang 2017-04-20 18:14
 * @version 1.0.0
 */
@Deprecated
public class ImplicitGrantHelper implements GlobalConstants {

    private static final Logger log = LoggerFactory.getLogger(RefreshAccessTokenHelper.class);

    private Client client;

    private OAuthHttpClient httpClient;

    public ImplicitGrantHelper(Client client) {
        this.client = client;
    }

    public ImplicitGrantHelper(Client client, OAuthHttpClient httpClient) {
        this.client = client;
        this.httpClient = httpClient;
    }

    /**
     * get implicit access token
     * since issue authorization code process must interact with resource owner, so we can't do it only in program
     *
     * @param skipConfirm
     * @param state
     * @return
     * @deprecated this method is error to use
     */
    @Deprecated
    public AccessToken getImplicitAccessToken(boolean skipConfirm, String state) {

        log.debug("Get implicit access token...");

        // prepare params
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(CLIENT_ID, String.valueOf(client.getId())));
        params.add(new BasicNameValuePair(REDIRECT_URI, client.getRedirectUri()));
        params.add(new BasicNameValuePair(RESPONSE_TYPE, ResponseType.IMPLICIT.getType()));
        if (ArrayUtils.isNotEmpty(client.getScopes())) {
            params.add(new BasicNameValuePair(SCOPE, StringUtils.join(client.getScopes(), " ")));
        }
        if (StringUtils.isNotBlank(state)) {
            params.add(new BasicNameValuePair(STATE, state));
        }
        params.add(new BasicNameValuePair(SKIP_CONFIRM, String.valueOf(skipConfirm)));

        /*
         * interactive process：
         *
         * 1. request https://account.xiaomi.com/oauth2/authorize with params
         * 2. interact with resource owner and authorize
         * 3. issued implicit access token as url fragment
         *
         */

        return null;
    }
}
