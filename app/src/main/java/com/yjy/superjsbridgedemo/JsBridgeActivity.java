package com.yjy.superjsbridgedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yjy.superbridge.internal.Bridge;
import com.yjy.superbridge.internal.BridgeInterceptor;
import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.BridgeWebView;
import com.yjy.superbridge.jsbridge.CallBackFunction;

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
                .registerInterface("JsTest",new JsTest())
                .addInterceptor(new BridgeInterceptor<String,CallBackFunction>() {
                    @Override
                    public boolean receiverInterceptor(String data, CallBackFunction function) {
                        Log.e("calljs","--------------");
                        return false;
                    }

                    @Override
                    public boolean sendInterceptor(String handlerName, String data, CallBackFunction callBack) {
                        Log.e("callNative","--------------");
                        return false;
                    }
                })
                .build();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bridge.callHandler("functionInJs","test test",new CallBackFunction(){
                    @Override
                    public void onCallBack(String data) {
                        Toast.makeText(JsBridgeActivity.this,data,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        bridge.registerHandler("test3", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack("response: data");
            }
        });



        view.loadUrl("file:///android_asset/demo.html");
    }
}
