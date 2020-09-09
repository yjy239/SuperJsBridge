package com.yjy.superjsbridgedemo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.yjy.converter.GsonConvertFactory;
import com.yjy.rnbridge.DefaultRnFactory;
import com.yjy.superbridge.internal.Bridge;
import com.yjy.superjsbridgedemo.bridge.JsTest;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.JSONMethodCodec;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.DefaultFlutterBridgeFactory;
import io.flutter.plugins.GeneratedPluginRegistrant;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/09/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class FlutterBridgeActivity extends FlutterActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bridge bridge =  new Bridge.Builder()
                .setClientFactory(new DefaultFlutterBridgeFactory(getFlutterView()))
                .setConvertFactory(GsonConvertFactory.create(new Gson()))
                .registerInterface("Sample",new JsTest())
                .build();
//        MethodChannel channel = new MethodChannel(getFlutterView(),"Sample");
//        channel.setMethodCallHandler(new MethodChannel.MethodCallHandler() {
//            @Override
//            public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
//                switch (call.method){
//                    case "readString":
//                        result.success("readString ok");
//                        break;
//                    case "showToast":
//                        Toast.makeText(FlutterBridgeActivity.this,call.arguments(),Toast.LENGTH_SHORT);
//                        result.success("ok");
//                        break;
//                    case "callbackTest":
//                        Log.e("callbackTest",call.arguments+" "+call.arguments.getClass());
//                        result.success("callbackTest ok");
//                        break;
//                    case "callbackArray":
//                        Log.e("callbackArray",call.arguments+"");
//                        result.success("callbackArray ok");
//                        break;
//                }
//            }
//        });
    }
}
