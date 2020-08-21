package com.yjy.superjsbridgedemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.yjy.converter.GsonConvertFactory;
import com.yjy.dsbridge.DSCompent.DSReceiveFromPlatformCallback;
import com.yjy.dsbridge.DSCompent.DefaultDsBridgeFactory;
import com.yjy.superbridge.internal.Bridge;
import com.yjy.superbridge.internal.BridgeInterceptor;
import com.yjy.superbridge.internal.model.ResponseData;
import com.yjy.superbridge.jsbridge.CallBackFunction;
import com.yjy.dsbridge.DSCompent.DSWebView;
import com.yjy.dsbridge.DSBridge.DWebView;
import com.yjy.dsbridge.DSBridge.OnReturnValue;


public class CallJavascriptActivity extends AppCompatActivity implements View.OnClickListener {

    DSWebView dWebView;
    private Bridge bridge;

    public <T extends View> T getView(int viewId) {
        View view = findViewById(viewId);
        return (T) view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_javascript);
        getView(R.id.addValue).setOnClickListener(this);
        getView(R.id.append).setOnClickListener(this);
        getView(R.id.startTimer).setOnClickListener(this);
        getView(R.id.synAddValue).setOnClickListener(this);
        getView(R.id.synGetInfo).setOnClickListener(this);
        getView(R.id.asynAddValue).setOnClickListener(this);
        getView(R.id.asynGetInfo).setOnClickListener(this);
        getView(R.id.hasMethodAddValue).setOnClickListener(this);
        getView(R.id.hasMethodXX).setOnClickListener(this);
        getView(R.id.hasMethodAsynAddValue).setOnClickListener(this);
        getView(R.id.hasMethodAsynXX).setOnClickListener(this);
        DWebView.setWebContentsDebuggingEnabled(true);
        dWebView= getView(R.id.webview);
        dWebView.loadUrl("file:///android_asset/native-call-js.html");

        //请注意DsBridge 为了接口统一，只暴露了统一的接口，单参数进入和回调接口
        bridge =  new Bridge.Builder()
                .setWebView(dWebView)
                .setClientFactory(new DefaultDsBridgeFactory(dWebView)
                        .setReceiveFromPlatformCallback(new DSReceiveFromPlatformCallback() {
                    @Override
                    public ResponseData<String> onFound(ResponseData<String> data) {
                        Log.e("ResponseData",data.getData());
                        return data;
                    }
                }))
                .setConvertFactory(GsonConvertFactory.create(new Gson()))
                .addInterceptor(new BridgeInterceptor() {
                    @Override
                    public boolean receiverInterceptor(String handlerName, Object data) {
                        Log.e("calljs",handlerName+"--------------"+data);
                        return true;
                    }

                    @Override
                    public boolean sendInterceptor(String handlerName, Object data) {
                        Log.e("callNative",handlerName+"--------------");
                        return false;
                    }
                })
                .build();



    }


    void showToast(Object o) {
        Toast.makeText(this, o.toString(), Toast.LENGTH_SHORT).show();
    }

    void showToast(String o) {
        Toast.makeText(this, o, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addValue:
                bridge.callHandler("addValue", "3", new CallBackFunction() {
                    @Override
                    public void complete(String data) {
                        showToast(data);
                    }
                },true);
                break;
            case R.id.append:
                dWebView.callHandler("append", new Object[]{"I", "love", "you"}, new OnReturnValue<String>() {
                    @Override
                    public void onValue(String retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.startTimer:
                bridge.callHandler("startTimer",null, new CallBackFunction(){
                    @Override
                    public void complete(String data) {
                        showToast(data);
                    }
                });
                break;
            case R.id.synAddValue:
                dWebView.callHandler("syn.addValue", new Object[]{5, 6}, new OnReturnValue<Integer>() {
                    @Override
                    public void onValue(Integer retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.synGetInfo:
                bridge.callHandler("syn.getInfo",null, new CallBackFunction(){
                    @Override
                    public void complete(String data) {
                        showToast(data);
                    }
                });
                break;
            case R.id.asynAddValue:
                dWebView.callHandler("asyn.addValue", new Object[]{5, 6}, new OnReturnValue<Integer>() {
                    @Override
                    public void onValue(Integer retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.asynGetInfo:
                bridge.callHandler("asyn.getInfo",null,  new CallBackFunction(){
                    @Override
                    public void complete(String data) {
                        showToast(data);
                    }
                });
                break;
            case R.id.hasMethodAddValue:
                dWebView.hasJavascriptMethod("addValue", new OnReturnValue<Boolean>() {
                    @Override
                    public void onValue(Boolean retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.hasMethodXX:
                dWebView.hasJavascriptMethod("XX", new OnReturnValue<Boolean>() {
                    @Override
                    public void onValue(Boolean retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.hasMethodAsynAddValue:
                dWebView.hasJavascriptMethod("asyn.addValue", new OnReturnValue<Boolean>() {
                    @Override
                    public void onValue(Boolean retValue) {
                        showToast(retValue);
                    }
                });
                break;
            case R.id.hasMethodAsynXX:
                dWebView.hasJavascriptMethod("asyn.XX", new OnReturnValue<Boolean>() {
                    @Override
                    public void onValue(Boolean retValue) {
                        showToast(retValue);
                    }
                });
                break;
        }

    }
}
