package com.yjy.superjsbridgedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yjy.converter.GsonConvertFactory;
import com.yjy.superbridge.internal.Bridge;
import com.yjy.superbridge.internal.BridgeInterceptor;
import com.yjy.superbridge.internal.CallBackHandler;
import com.yjy.superbridge.internal.model.ResponseData;
import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.BridgeWebView;
import com.yjy.superbridge.jsbridge.CallBackFunction;
import com.yjy.superbridge.jsbridge.DefaultJsBridgeFactory;
import com.yjy.superbridge.jsbridge.JSReceiveFromPlatformCallback;
import com.yjy.superbridge.jsbridge.Message;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/11
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class JsBridgeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_bridge);
        final BridgeWebView view = findViewById(R.id.webView);
//
        final Bridge bridge =  new Bridge.Builder(view)
                .setClientFactory(new DefaultJsBridgeFactory(view)
                .setReceiveFromPlatformCallback(new JSReceiveFromPlatformCallback() {
                    @Override
                    public ResponseData<Message> onFound(ResponseData<Message> data) {
                        Log.e("onFound",data.getData().toJson());
                        return data;
                    }
                }))
                .setConvertFactory(GsonConvertFactory.create(new Gson()))
                .registerInterface(null,new JsTest())
                .addInterceptor(new BridgeInterceptor<String,CallBackFunction>() {
                    @Override
                    public boolean receiverInterceptor(String handlerName, String data) {
                        Log.e("calljs1","--------------"+data);
                        return false;
                    }

                    @Override
                    public boolean sendInterceptor(String handlerName, Object data) {
                        Log.e("callNative1",handlerName+"--------------"+data);
                        return false;
                    }
                })
                .addInterceptor(new BridgeInterceptor() {
                    @Override
                    public boolean receiverInterceptor(String handlerName, Object data) {
                        Log.e("calljs2","--------------"+data);
                        return false;
                    }

                    @Override
                    public boolean sendInterceptor(String handlerName, Object data) {
                        Log.e("callNative2","--------------"+data);
                        return false;
                    }
                })
                .build();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bridge.callHandler("functionInJs","test test",new CallBackFunction(){
                    @Override
                    public void complete(String data) {
                        Toast.makeText(JsBridgeActivity.this,data,Toast.LENGTH_SHORT).show();
                    }
                },false);
            }
        });

//        bridge.registerHandler("submitFromWeb", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackHandler<String> function) {
//                function.complete("response: data");
//            }
//        },true);



        view.loadUrl("file:///android_asset/demo.html");
    }
}
