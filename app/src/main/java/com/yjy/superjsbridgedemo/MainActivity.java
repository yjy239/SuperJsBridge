package com.yjy.superjsbridgedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        final DSWebView view = findViewById(R.id.webView);
//
//        Bridge bridge =  new Bridge.Builder(view)
//                .setClientAndCore(new DSCore(view),new DSWebClient())
//                .registerInterface("JsTest",new JsTest())
//                .registerInterface(null,new JsApi())
//                .registerInterface("echo",new JsEchoApi())
//                .build(new Bridge.IBuilder() {
//                    @Override
//                    public void build(String name, Object obj, IBridgeCore core) {
//                        ((DSCore)core).addJsObject(obj,name);
//                    }
//                });
//
//
//        view.loadUrl("file:///android_asset/js-call-native.html");

        findViewById(R.id.callJs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CallJavascriptActivity.class));
            }
        });
        findViewById(R.id.callNative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,JavascriptCallNativeActivity.class));
            }
        });
        findViewById(R.id.fly).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,JsBridgeActivity.class));
            }
        });
    }





}
