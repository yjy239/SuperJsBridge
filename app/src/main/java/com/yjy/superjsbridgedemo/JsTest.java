package com.yjy.superjsbridgedemo;

import android.net.Uri;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.yjy.rnbridge.RnBridge.PromiseCallback;
import com.yjy.superbridge.internal.BridgeField;
import com.yjy.superbridge.internal.BridgeInterface;
import com.yjy.superbridge.internal.BridgeMethod;
import com.yjy.superbridge.internal.CallBackHandler;
import com.yjy.superbridge.jsbridge.CallBackFunction;
import com.yjy.superjsbridgedemo.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.CallbackHandler;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/11
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class JsTest extends BridgeInterface {

//    @BridgeMethod(interceptor = true)
//    public void test1(String s){
//        Log.e("JSTest",s) ;
//    }

//    @BridgeMethod(interceptor = true)
//    public void submitFromWeb(@BridgeField(name = "param") String data, CallBackFunction function){
//        function.complete("submitFromWeb response: "+data);
//    }
//
//    @BridgeMethod(interceptor = true)
//    public void submitUserFromWeb(@BridgeField(name = "user") User data, CallBackFunction function){
//        function.complete("submitFromWeb response: "+data.getUsername());
//    }

    @BridgeMethod(interceptor = true)
    public void readString(String s){
        Log.e("JSTest",s) ;
    }




    @BridgeMethod(interceptor = true)
    public void promiseTest(User map, PromiseCallback promise){
        Log.e("user",map.name);
        promise.resolve("success");
    }

//    @BridgeMethod(interceptor = true)
//    public void callbackArray(ReadableArray array, PromiseCallback promise){
//        int size = array.size();
//        for (int i = 0; i < size; i++) {
//            ReadableMap map = array.getMap(i);
//            if (map != null && map.hasKey("uri")) {
//                String item = map.getString("uri");
//                String path = Uri.parse(item).getPath();
//                Log.e("callbackArray",path);
//            }
//        }
//
//        promise.resolve("Array success");
//    }

    @BridgeMethod(interceptor = true)
    public void promiseArray(ArrayList<URL> array, PromiseCallback promise){
        int size = array.size();
        for (int i = 0; i < size; i++) {
            URL map = array.get(i);
            if (map != null ) {
                String item = map.uri;
                String path = Uri.parse(item).getPath();
                Log.e("callbackArray",path);
            }
        }

        promise.resolve("Array success");
    }

    @BridgeMethod(interceptor = true)
    public void callBackArray(ArrayList<URL> array, CallBackHandler callbackHandler){
        int size = array.size();
        for (int i = 0; i < size; i++) {
            URL map = array.get(i);
            if (map != null ) {
                String item = map.uri;
                String path = Uri.parse(item).getPath();
                Log.e("callbackArray",path);
            }
        }

        callbackHandler.complete("callBackArray success");
    }

    @BridgeMethod(interceptor = true)
    public void callbackTest(User map, CallBackHandler callbackHandler){
        Log.e("user",map.name);
        callbackHandler.complete("callbackTest success");
    }


    class User{
        String name;
    }

    class URL{
        String uri;
    }


}
