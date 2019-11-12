package com.yjy.superbridge.internal;

import android.webkit.JavascriptInterface;
import android.webkit.WebViewClient;

import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.BridgeWebViewClient;
import com.yjy.superbridge.jsbridge.CallBackFunction;
import com.yjy.superbridge.jsbridge.JsBridgeCore;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/11
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class BridgeHelper {
    public static void init(IWebView webView,Object obj,String name,boolean isLow){
        JsBridgeCore core = new JsBridgeCore(webView);
        if(isLow){
            registerInLow(obj,core);
        }else {
            webView.addJavascriptInterface(obj,name);
        }

        WebViewClient client = new BridgeWebViewClient(core);
        webView.setWebViewClient(client);
    }


    public static void registerInLow(Object obj,IBridgeCore core){
        if(obj != null){
            Method[] methods = obj.getClass().getDeclaredMethods();
            for (int i = 0;i<methods.length;i++){
                final Method method = methods[i];
                Type type = method.getReturnType();
                if(type == void.class||type==String.class){
                    register(obj,type,method,core);
                }else {
                    throw new IllegalArgumentException("return type must be String or Void");
                }
            }
        }

    }


    private static void register(final Object obj, final Type type, final Method method, IBridgeCore core){
        BridgeHandler handler = null;


        if(type == void.class){
            handler = new BridgeHandler() {
                @Override
                public void handler(String data, CallBackFunction function) {
                    try {
                        Type[] types = method.getGenericParameterTypes();
                        if(types.length == 0){
                            method.invoke(obj);
                        }else if(types.length == 1&&types[0] == String.class){
                            method.invoke(obj,data);
                        }else if(types.length == 2
                                &&types[0] == String.class
                                &&types[1] == CallBackFunction.class){
                            method.invoke(obj,data,function);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            };

        }else if(type == String.class){
            handler = new BridgeHandler() {
                @Override
                public void handler(String data, CallBackFunction function) {
                    try {
                        String result = (String)method.invoke(obj,data);
                        function.onCallBack(result);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            };
        }

        core.registerHandler(method.getName(), handler);

    }
}
