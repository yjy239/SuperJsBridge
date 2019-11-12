package com.yjy.superjsbridgedemo.DSCompent;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yjy.superbridge.jsbridge.BridgeUtil;
import com.yjy.superbridge.jsbridge.JsBridgeCore;
import com.yjy.superbridge.jsbridge.Message;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/11
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class DSWebClient extends WebViewClient {


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
      //  BridgeUtil.webViewLoadLocalJs(view, "dsbridge.js");

    }
}
