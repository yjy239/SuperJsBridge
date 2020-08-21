package com.yjy.superjsbridgedemo.rn;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@ReactModule(name = SampleRnJavaModule.NAME)
public class SampleRnJavaModule extends ReactContextBaseJavaModule {
    public static final String NAME = "Sample";

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void read(String map){
        Log.e("map",map);
    }

    @ReactMethod
    public void callback(ReadableMap map,Promise promise){
        Log.e("map",map.getString("name"));
        promise.resolve("success");
    }


    @ReactMethod
    public void callbackArray(ReadableArray array, Promise promise){
        int size = array.size();
        for (int i = 0; i < size; i++) {
            ReadableMap map = array.getMap(i);
            if (map != null && map.hasKey("uri")) {
                String item = map.getString("uri");
                String path = Uri.parse(item).getPath();
                Log.e("callbackArray",path);
            }
        }

        promise.resolve("Array success");
    }
}
