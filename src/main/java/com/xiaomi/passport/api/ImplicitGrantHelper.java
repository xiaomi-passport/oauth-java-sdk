package com.xiaomi.passport.api;

import com.xiaomi.passport.common.OAuthHttpClient;
import com.xiaomi.passport.common.ResponseType;
import com.xiaomi.passport.constant.GlobalConstants;
import com.xiaomi.passport.exception.OAuthSdkException;
import com.xiaomi.passport.pojo.AccessToken;
import com.xiaomi.passport.pojo.Client;
import com.xiaomi.passport.util.AuthorizeUrlUtils;
import com.xiaomi.passport.util.HttpResponseUtils;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * implicit grant type
 *
 * @author zhenchao.wang 2017-04-20 18:14
 * @version 1.0.0
 */
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

        HttpResponse response;
        try {
            response = httpClient.get(AuthorizeUrlUtils.getAuthorizeUrl(), params);
        } catch (OAuthSdkException e) {
            log.error("Get authorization code error when execute http request!", e);
            return null;
        }
        if (!HttpResponseUtils.isRedirectStatusLine(response)) {
            log.error("The http response status[{}] is not expected, expected redirect http status!", response.getStatusLine());
            return null;
        }

        Header header = response.getFirstHeader("Location");
        if (StringUtils.isBlank(header.getValue()) || UrlValidator.getInstance().isValid(header.getValue())) {
            log.error("Get authorization code error, the code redirect response [{}] is not expected!", header.getValue());
            return null;
        }

        try {
            URIBuilder builder = new URIBuilder(header.getValue());
            String fragment = builder.getFragment();
            if (StringUtils.isNotBlank(fragment) && JSONUtils.mayBeJSON(fragment)) {
                return new AccessToken(JSONObject.fromObject(fragment));
            }
        } catch (URISyntaxException e) {
            // never happen
        }
        return null;
    }
}
