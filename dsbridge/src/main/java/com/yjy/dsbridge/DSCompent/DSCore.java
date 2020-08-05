package com.yjy.dsbridge.DSCompent;

import com.yjy.superbridge.internal.BaseBridgeCore;
import com.yjy.superbridge.internal.BridgeHelper;
import com.yjy.superbridge.internal.CallBackHandler;
import com.yjy.superbridge.internal.MethodMap;
import com.yjy.superbridge.internal.ProxyHandler;
import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.CallBackFunction;
import com.yjy.dsbridge.DSBridge.DWebView;
import com.yjy.dsbridge.DSBridge.OnReturnValue;

import java.util.ArrayList;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/11
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class DSCore extends BaseBridgeCore {
    private DWebView mWebView;

    public DSCore(DWebView webView){
        this.mWebView = webView;
        mWebView.setCore(this);
        mWebView.addInternalJavascriptObject();
    }

    @Override
    public void registerHandler(final String handlerName, final BridgeHandler handler, final boolean isInterceptor) {
        if(mWebView == null){
            return;
        }
        register(handlerName, new ProxyHandler(this,handlerName,handler));

    }

    @Override
    public void unregisterHandler(String handlerName) {
        if(mWebView == null){
            return;
        }
        unregister(handlerName);
    }

    public void addJsObject(Object obj,String name){
        if(mWebView == null){
            return;
        }
        mWebView.addJavascriptObject(obj,name);
    }

    @Override
    public void callHandler(String handlerName, final String data, final CallBackFunction callBack, boolean isInterceptor) {
        if(mWebView == null){
            return;
        }
        Object[] args = {handlerName,data,callBack};
        if(BridgeHelper.iterSendInterceptor(this,args)){
            return;
        }

        OnReturnValue retValue = new OnReturnValue<Object>() {
            @Override
            public void onValue(Object retValue) {
                if(callBack != null){
                    if(retValue instanceof String){
                        callBack.complete((String)retValue);
                    }else {
                        callBack.complete(retValue == null?null:retValue.toString());
                    }
                }
            }
        };

        if(data == null){
            mWebView.callHandler(handlerName,retValue );
        }else{
            mWebView.callHandler(handlerName,new Object[]{data},retValue);
        }
    }

    @Override
    public void registerObj(String name, Object obj) {
        addJsObject(obj,name);
    }

    @Override
    public void release() {
        mWebView = null;
    }
}
