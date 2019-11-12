package com.yjy.superjsbridgedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yjy.superbridge.internal.Bridge;
import com.yjy.superbridge.internal.IBridgeCore;
import com.yjy.superbridge.jsbridge.BridgeWebView;
import com.yjy.superjsbridgedemo.DSCompent.DSCore;
import com.yjy.superjsbridgedemo.DSCompent.DSWebClient;
import com.yjy.superjsbridgedemo.DSCompent.DSWebView;
import com.yjy.superjsbridgedemo.DSCompent.JsApi;
import com.yjy.superjsbridgedemo.DSCompent.JsEchoApi;

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
        Bridge bridge =  new Bridge.Builder(view)
                .registerInterface("JsTest",new JsTest())
                .build();

        view.loadUrl("file:///android_asset/js-call-native.html");
    }
}
