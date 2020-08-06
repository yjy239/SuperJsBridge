package com.yjy.superjsbridgedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.yjy.converter.GsonConvertFactory;
import com.yjy.dsbridge.DSCompent.DSReceiveFromPlatformCallback;
import com.yjy.dsbridge.DSCompent.DefaultDsBridgeFactory;
import com.yjy.superbridge.internal.Bridge;
import com.yjy.superbridge.internal.BridgeInterceptor;
import com.yjy.dsbridge.DSBridge.CompletionHandler;
import com.yjy.dsbridge.DSCompent.DSWebView;
import com.yjy.superbridge.internal.model.ResponseData;


public class JavascriptCallNativeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_native);
        final DSWebView dwebView= (DSWebView) findViewById(R.id.webview);
        // set debug mode
        DSWebView.setWebContentsDebuggingEnabled(true);
        Bridge bridge =  new Bridge.Builder(dwebView)
                .setClientFactory(new DefaultDsBridgeFactory(dwebView)
                .setReceiveFromPlatformCallback(new DSReceiveFromPlatformCallback() {
                    @Override
                    public ResponseData<String> onFound(ResponseData<String> data) {
                        Log.e("ResponseData",data.getData());
                        return data;
                    }
                }))
                .setConvertFactory(GsonConvertFactory.create(new Gson()))
                .registerInterface("JsTest",new JsTest())
                .registerInterface(null,new JsApi())
                .registerInterface("echo",new JsEchoApi())
                .addInterceptor(new BridgeInterceptor<Object,CompletionHandler>() {
                    @Override
                    public boolean receiverInterceptor(String handlerName, Object data) {
                        Log.e("calljs",handlerName+"--------------"+data);
                        return false;
                    }

                    @Override
                    public boolean sendInterceptor(String handlerName, Object data) {
                        Log.e("callNative",handlerName+"--------------"+data);
                        return false;
                    }
                })
                .build();

        dwebView.loadUrl("file:///android_asset/js-call-native.html");
    }
}
