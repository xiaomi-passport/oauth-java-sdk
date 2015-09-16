import com.xiaomi.api.http.XMApiHttpClient;
import com.xiaomi.api.http.XMException;
import com.xiaomi.api.http.XMHttpClient;
import com.xiaomi.api.http.XMOAuthHttpClient;
import com.xiaomi.utils.AccessToken;
import com.xiaomi.utils.XMUtil;

import org.apache.commons.httpclient.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class HttpTest {
    static long clientId = your clientId;
    static String clientKey = "your key";
    XMHttpClient xmHttpClient = new XMHttpClient();

    @Test
    public void testXMOAuthHttpClient() throws XMException, URISyntaxException {
        String redirectUri = "http://xiaomi.com";
        XMOAuthHttpClient client = new XMOAuthHttpClient(clientId, clientKey, redirectUri, xmHttpClient);
        String url = client.getAuthorizeUrl();
        System.out.println(url);
    }

    @Test
    public void testXMApiHttpClient() throws XMException, URISyntaxException, JSONException, UnsupportedEncodingException,
                                     InvalidKeyException, NoSuchAlgorithmException {

        // mac
        String tokenId = "token id";

        List<Header> headers = new ArrayList<Header>();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("clientId", String.valueOf(clientId)));
        params.add(new BasicNameValuePair("token", tokenId));

        String macKey = "mac key";
        
        String nonce = XMUtil.generateNonce();
        String qs = URLEncodedUtils.format(params, "UTF-8");
        String mac = XMUtil.getMacAccessTokenSignatureString(nonce, "GET", "open.account.xiaomi.com", "/user/profile", qs,
            macKey, "HmacSHA1");
        Header macHeader = XMUtil.buildMacRequestHead(tokenId, nonce, mac);

        headers.add(macHeader);

        XMApiHttpClient client = new XMApiHttpClient(clientId, tokenId, xmHttpClient);
        JSONObject json = client.apiCall("/user/profile", params, headers, "GET");
        System.out.println(json.toString());
    }
}
