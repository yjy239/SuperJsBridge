package com.yjy.superjsbridgedemo.DSCompent;

import android.os.CountDownTimer;
import android.webkit.JavascriptInterface;

import com.yjy.superbridge.internal.BridgeInterface;
import com.yjy.superbridge.internal.ReceiverBridge;
import com.yjy.superjsbridgedemo.DSBridge.CompletionHandler;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by du on 16/12/31.
 */

public class JsApi extends BridgeInterface {
    @JavascriptInterface
    @ReceiverBridge
    public String testSyn(Object msg)  {
        return msg + "［syn call］";
    }

    @JavascriptInterface
    @ReceiverBridge
    public void testAsyn(Object msg, CompletionHandler<String> handler){
        handler.complete(msg+" [ asyn call]");
    }

    @JavascriptInterface
    @ReceiverBridge
    public String testNoArgSyn(Object arg) throws JSONException {
        return  "testNoArgSyn called [ syn call]";
    }

    @JavascriptInterface
    @ReceiverBridge
    public void testNoArgAsyn(Object arg, CompletionHandler<String> handler) {
        handler.complete( "testNoArgAsyn   called [ asyn call]");
    }


    @JavascriptInterface
    //without @JavascriptInterface annotation can't be called
    @ReceiverBridge
    public String testNever(Object arg) throws JSONException {
        JSONObject jsonObject= (JSONObject) arg;
        return jsonObject.getString("msg") + "[ never call]";
    }

    @JavascriptInterface
    @ReceiverBridge
    public void callProgress(Object args, final CompletionHandler<Integer> handler) {

        new CountDownTimer(11000, 1000) {
            int i=10;
            @Override
            public void onTick(long millisUntilFinished) {
                //setProgressData can be called many times util complete be called.
                handler.setProgressData((i--));

            }
            @Override
            public void onFinish() {
                //complete the js invocation with data; handler will be invalid when complete is called
                handler.complete(0);

            }
        }.start();
    }
}