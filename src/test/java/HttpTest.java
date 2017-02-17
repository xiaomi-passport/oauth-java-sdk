import com.xiaomi.api.http.XMApiHttpClient;
import com.xiaomi.api.http.XMHttpClient;
import com.xiaomi.api.http.XMOAuthHttpClient;
import com.xiaomi.utils.XMUtil;
import org.apache.commons.httpclient.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class HttpTest {

    // your client id
    private static final long CLIENT_ID = -1L;

    // your client secret
    private static final String CLIENT_SECRET = "your client secret";

    private XMHttpClient xmHttpClient = new XMHttpClient();

    @Test
    public void testXMOAuthHttpClient() throws Exception {
        String redirectUri = "http://xiaomi.com";
        XMOAuthHttpClient client = new XMOAuthHttpClient(CLIENT_ID, CLIENT_SECRET, redirectUri, xmHttpClient);
        String url = client.getAuthorizeUrl();
        System.out.println(url);
    }

    @Test
    public void testXMApiHttpClient() throws Exception {

        // mac
        String tokenId = "token id";

        List<Header> headers = new ArrayList<Header>();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("clientId", String.valueOf(CLIENT_ID)));
        params.add(new BasicNameValuePair("token", tokenId));

        String macKey = "mac key";

        String nonce = XMUtil.generateNonce();
        String qs = URLEncodedUtils.format(params, "UTF-8");
        String mac = XMUtil.getMacAccessTokenSignatureString(
                nonce, "GET", "open.account.xiaomi.com", "/user/profile", qs, macKey, "HmacSHA1");
        Header macHeader = XMUtil.buildMacRequestHead(tokenId, nonce, mac);
        headers.add(macHeader);
        XMApiHttpClient client = new XMApiHttpClient(CLIENT_ID, tokenId, xmHttpClient);
        JSONObject json = client.apiCall("/user/profile", params, headers, "GET");
        System.out.println(json.toString());
    }
}
