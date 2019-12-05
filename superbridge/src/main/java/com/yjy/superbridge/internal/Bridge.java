package com.yjy.superbridge.internal;

import android.webkit.WebViewClient;

import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.BridgeWebViewClient;
import com.yjy.superbridge.jsbridge.CallBackFunction;
import com.yjy.superbridge.jsbridge.JsBridgeCore;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class Bridge {
    private IBridgeCore mCore;

    private Bridge(IBridgeCore core){
        this.mCore = core;
    }


    public void registerHandler(String handlerName, BridgeHandler handler) {
        registerHandler(handlerName, handler,false);
    }


    public void registerHandler(String handlerName, BridgeHandler handler,boolean isInterceptor) {
        if(mCore == null){
            return;
        }
        mCore.registerHandler(handlerName,handler,isInterceptor);
    }

    public void unregisterHandler(String handlerName) {
        if(mCore == null){
            return;
        }
        mCore.unregisterHandler(handlerName);
    }


    public void callHandler(String handlerName, String data, CallBackFunction callBack,boolean isInterceptor) {
        if(mCore == null){
            return;
        }
        mCore.callHandler(handlerName,data,callBack,isInterceptor);
    }

    public void callHandler(String handlerName, String data, CallBackFunction callBack) {
        callHandler(handlerName,data,callBack,false);
    }


    public void addInterceptor(BridgeInterceptor interceptor){
        if(mCore == null){
            return;
        }
        mCore.addInterceptor(interceptor);
    }


    public void removeInterceptor(BridgeInterceptor interceptor){
        if(mCore == null){
            return;
        }
        mCore.removeInterceptor(interceptor);
    }


    public IBridgeCore getCore() {
        return mCore;
    }

    public void setCore(IBridgeCore mCore) {
        this.mCore = mCore;
    }


    public void release(){
        mCore.release();
        mCore = null;
    }

    public static class Builder{
        private IWebView webView;
        Map<String,BridgeInterface> observableMap = new HashMap<>();
        WebViewClient client;
        IBridgeCore core;
        ArrayList<BridgeInterceptor> mInterceptors = new ArrayList<>();
        public Builder(IWebView webView){
            this.webView = webView;
        }



        public Builder registerInterface(String name,BridgeInterface object){

            observableMap.put(name,object);
            return this;
        }

        public Builder setClientAndCore(IBridgeCore core,WebViewClient client){
            this.client = client;
            this.core = core;
            return this;
        }


        public Builder addInterceptor(BridgeInterceptor interceptor){
            mInterceptors.add(interceptor);
            return this;
        }


        public Bridge build(){
            if(core == null||client == null){
                core = new JsBridgeCore(webView);
                client = new BridgeWebViewClient((JsBridgeCore)core);
            }

            core.setInterceptor(mInterceptors);

            for(Map.Entry<String,BridgeInterface> entry : observableMap.entrySet()){
                entry.getValue().setInterceptors(mInterceptors);
                core.registerObj(entry.getKey(),entry.getValue());
            }



            webView.setWebViewClient(client);

            return new Bridge(core);

        }





    }


    public interface IBuilder{
        void build(String name,Object obj,IBridgeCore core);
    }
}
