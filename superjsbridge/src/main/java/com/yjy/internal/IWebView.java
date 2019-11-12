package com.yjy.internal;

import android.content.Context;
import android.webkit.WebViewClient;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/08
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public interface IWebView {

    Context getContext();

    void loadUrl(String url);

    void setWebViewClient(WebViewClient client);
}
