import com.xiaomi.passport.api.OAuthAuthorizeHelper;
import com.xiaomi.passport.api.OpenApiHelper;
import com.xiaomi.passport.common.HttpMethod;
import com.xiaomi.passport.common.HttpRequestClient;
import com.xiaomi.passport.util.CommonUtils;
import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class HttpTest {

    // your client id
    private static final long CLIENT_ID = -1L;

    // your client secret
    private static final String CLIENT_SECRET = "your client secret";

    private HttpRequestClient httpRequestClient = new HttpRequestClient();

    @Test
    public void testXMOAuthHttpClient() throws Exception {
        String redirectUri = "http://xiaomi.com";
        OAuthAuthorizeHelper client = new OAuthAuthorizeHelper(CLIENT_ID, CLIENT_SECRET, redirectUri, httpRequestClient);
        String url = client.getCodeAuthorizeUrl();
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

        String nonce = CommonUtils.generateNonce();
        String qs = URLEncodedUtils.format(params, "UTF-8");
        String mac = CommonUtils.getMacAccessTokenSignature(
                nonce, "GET", "open.account.xiaomi.com", "/user/profile", qs, macKey, "HmacSHA1");
        Header macHeader = CommonUtils.buildMacRequestHeader(tokenId, nonce, mac);
        headers.add(macHeader);
        OpenApiHelper client = new OpenApiHelper(CLIENT_ID, tokenId, httpRequestClient);
        JSONObject json = client.request("/user/profile", HttpMethod.GET, params, headers);
        System.out.println(json.toString());
    }
}
