package com.yjy.superbridge.jsbridge;

import com.yjy.superbridge.internal.IBridgeClient;
import com.yjy.superbridge.internal.IWebView;
import com.yjy.superbridge.internal.IBridgeCore;
import com.yjy.superbridge.internal.IBridgeFactory;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DefaultJsBridgeFactory implements IBridgeFactory<JSReceiveFromPlatformCallback> {

    private IWebView webView;
    private JsBridgeCore mCore;
    private JSReceiveFromPlatformCallback callback;
    public DefaultJsBridgeFactory(IWebView webView){
        this.webView = webView;
    }

    @Override
    public IBridgeCore getBridgeCore() {
        if(mCore == null){
            mCore = new JsBridgeCore(webView);
        }
        return mCore;
    }

    @Override
    public IBridgeClient getBridgeClient() {
        return new BridgeWebViewClient((JsBridgeCore)getBridgeCore());
    }

    @Override
    public IBridgeFactory setReceiveFromPlatformCallback(JSReceiveFromPlatformCallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public JSReceiveFromPlatformCallback getReceiveFromPlatformCallback() {
        return callback;
    }


}
