package com.yjy.superbridge.internal;

import android.webkit.WebViewClient;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/11
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public interface IWebView {
    void loadUrl(String url);
    void setWebViewClient(WebViewClient client);
    void addJavascriptInterface(Object object,String name);
}
