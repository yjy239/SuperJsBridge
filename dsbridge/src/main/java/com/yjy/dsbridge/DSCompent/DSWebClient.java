package com.yjy.dsbridge.DSCompent;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yjy.superbridge.internal.IBridgeClient;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/11
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class DSWebClient extends WebViewClient implements IBridgeClient {


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

    }

    @Override
    public String getName() {
        return null;
    }
}
