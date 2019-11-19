package com.yjy.superjsbridgedemo.DSCompent;

import android.webkit.JavascriptInterface;

import com.yjy.superbridge.internal.BridgeInterface;
import com.yjy.superbridge.internal.ReceiverBridge;
import com.yjy.superjsbridgedemo.DSBridge.CompletionHandler;

import org.json.JSONException;



/**
 * Created by du on 16/12/31.
 */

public class JsEchoApi extends BridgeInterface {

    @JavascriptInterface
    @ReceiverBridge
    public Object syn(Object args) throws JSONException {
        return  args;
    }

    @JavascriptInterface
    @ReceiverBridge
    public void asyn(Object args, CompletionHandler handler){
        handler.complete(args);
    }
}