package com.yjy.dsbridge.DSCompent;

import com.yjy.superbridge.internal.IBridgeClient;
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
public class DefaultDsBridgeFactory implements IBridgeFactory {
    private DSWebView mDsWebView;
    private DSCore mCore;
    public DefaultDsBridgeFactory(DSWebView webview){
        this.mDsWebView = webview;
    }
    @Override
    public IBridgeCore getBridgeCore() {
        if(mCore == null){
            mCore = new DSCore(mDsWebView);
        }
        return mCore;
    }

    @Override
    public IBridgeClient getBridgeClient() {
        return new DSWebClient();
    }
}
