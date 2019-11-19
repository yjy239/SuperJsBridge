package com.yjy.superjsbridgedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yjy.superbridge.internal.Bridge;
import com.yjy.superbridge.internal.BridgeInterceptor;
import com.yjy.superbridge.internal.IBridgeCore;
import com.yjy.superbridge.jsbridge.CallBackFunction;
import com.yjy.superjsbridgedemo.DSBridge.CompletionHandler;
import com.yjy.superjsbridgedemo.DSCompent.DSCore;
import com.yjy.superjsbridgedemo.DSCompent.DSWebClient;
import com.yjy.superjsbridgedemo.DSCompent.DSWebView;
import com.yjy.superjsbridgedemo.DSCompent.JsApi;
import com.yjy.superjsbridgedemo.DSCompent.JsEchoApi;

import org.json.JSONObject;


public class JavascriptCallNativeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_native);
        final DSWebView dwebView= (DSWebView) findViewById(R.id.webview);
        // set debug mode
        DSWebView.setWebContentsDebuggingEnabled(true);
        Bridge bridge =  new Bridge.Builder(dwebView)
                .setClientAndCore(new DSCore(dwebView),new DSWebClient())
                .registerInterface("JsTest",new JsTest())
                .registerInterface(null,new JsApi())
                .registerInterface("echo",new JsEchoApi())
                .addInterceptor(new BridgeInterceptor<Object,CompletionHandler>() {
                    @Override
                    public boolean receiverInterceptor(Object data, CompletionHandler function) {
                        Log.e("calljs","--------------"+function);
                        return true;
                    }

                    @Override
                    public boolean sendInterceptor(String handlerName, String data, CallBackFunction callBack) {
                        Log.e("callNative","--------------");
                        return true;
                    }
                })
                .build(new Bridge.IBuilder() {
                    @Override
                    public void build(String name, Object obj, IBridgeCore core) {
                        ((DSCore)core).addJsObject(obj,name);
                    }
                });

        dwebView.loadUrl("file:///android_asset/js-call-native.html");
    }
}
