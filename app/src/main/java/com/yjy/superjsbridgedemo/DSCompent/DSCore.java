package com.yjy.superjsbridgedemo.DSCompent;

import com.yjy.superbridge.internal.BaseBridgeCore;
import com.yjy.superbridge.internal.IBridgeCore;
import com.yjy.superbridge.internal.SendBridge;
import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.CallBackFunction;
import com.yjy.superjsbridgedemo.DSBridge.DWebView;
import com.yjy.superjsbridgedemo.DSBridge.OnReturnValue;

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
    ArrayList<String> mKeys = new ArrayList<>();

    public DSCore(DWebView webView){
        this.mWebView = webView;
    }

    @Override
    public void registerHandler(String handlerName, BridgeHandler handler) {
        if(mWebView == null){
            return;
        }


    }

    @Override
    public void unregisterHandler(String handlerName) {
        if(mWebView == null){
            return;
        }
        mWebView.removeJavascriptObject(handlerName);
    }

    public void addJsObject(Object obj,String name){
        if(mWebView == null){
            return;
        }
        mKeys.add(name);
        mWebView.addJavascriptObject(obj,name);
    }

    @Override
    @SendBridge
    public void callHandler(String handlerName, final String data, final CallBackFunction callBack) {
        if(mWebView == null){
            return;
        }


        OnReturnValue retValue = new OnReturnValue<Object>() {
            @Override
            public void onValue(Object retValue) {
                if(callBack != null){
                    if(retValue instanceof String){
                        callBack.onCallBack((String)retValue);
                    }else {
                        callBack.onCallBack(retValue == null?null:retValue.toString());
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
    public void release() {
    }
}
