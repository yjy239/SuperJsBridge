package com.yjy.dsbridge.DSCompent;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebViewClient;

import com.yjy.superbridge.internal.IBridgeClient;
import com.yjy.superbridge.internal.IWebView;
import com.yjy.dsbridge.DSBridge.DWebView;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/11
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class DSWebView extends DWebView implements IWebView {
    public DSWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DSWebView(Context context) {
        super(context);
    }

    @Override
    public void setClient(IBridgeClient client) {
        if(client instanceof WebViewClient){
            setWebViewClient((WebViewClient)client);
        }

    }
}
