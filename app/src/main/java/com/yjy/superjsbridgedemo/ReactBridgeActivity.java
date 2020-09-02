package com.yjy.superjsbridgedemo;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.ReactBridge;
import com.google.gson.Gson;
import com.yjy.converter.GsonConvertFactory;
import com.yjy.rnbridge.DefaultRnFactory;
import com.yjy.superbridge.internal.Bridge;
import com.yjy.superbridge.internal.BridgeInterceptor;
import com.yjy.superbridge.internal.CallBackHandler;
import com.yjy.superbridge.internal.model.ResponseData;
import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.CallBackFunction;
import com.yjy.superbridge.jsbridge.DefaultJsBridgeFactory;
import com.yjy.superbridge.jsbridge.JSReceiveFromPlatformCallback;
import com.yjy.superbridge.jsbridge.Message;
import com.yjy.superjsbridgedemo.rn.JSModule;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ReactBridgeActivity extends ReactActivity {
    @Override
    protected String getMainComponentName() {
        return "SuperBridge";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bridge bridge =  new Bridge.Builder()
                .setClientFactory(new DefaultRnFactory(getReactInstanceManager()))
                .setConvertFactory(GsonConvertFactory.create(new Gson()))
                .registerInterface("Sample",new JsTest())
                .build();


        bridge.registerHandler("registerTest", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackHandler<String> function) {
                Log.e("result",data);

                function.complete("registerTest success");
            }
        });

        bridge.registerHandler("calHandlerTest", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackHandler<String> function) {
                Log.e("result",data);

                bridge.callHandler("JSModule.callHandler","send msg",null);

                function.complete("calHandlerTest success");
            }
        });
    }
}
