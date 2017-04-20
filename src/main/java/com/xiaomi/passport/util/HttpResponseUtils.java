package com.xiaomi.passport.util;

import com.xiaomi.passport.constant.GlobalConstants;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * http response utils
 *
 * @author zhenchao.wang 2017-04-20 16:18
 * @version 1.0.0
 */
public class HttpResponseUtils {

    private HttpResponseUtils() {
    }

    /**
     * if status is 302, 303, or 307
     *
     * @param response
     * @return
     */
    public static boolean isRedirectStatusLine(HttpResponse response) {
        if (null == response) {
            return false;
        }
        int status = response.getStatusLine().getStatusCode();
        return 302 == status || 303 == status || 307 == status;
    }

    /**
     * get http response entity as string format
     *
     * @param response
     * @return empty string when no content or error content
     */
    public static String getEntityContent(HttpResponse response) {
        if (null == response || null == response.getEntity()) {
            return StringUtils.EMPTY;
        }
        try {
            return StringUtils.replace(IOUtils.toString(response.getEntity().getContent(),
                    GlobalConstants.DEFAULT_CHARSET), GlobalConstants.JSON_SAFE_FLAG, StringUtils.EMPTY);
        } catch (IOException e) {
            // return empty string
        }
        return StringUtils.EMPTY;
    }
}
