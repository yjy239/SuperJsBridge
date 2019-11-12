package com.yjy.superbridge.internal;

import android.os.Build;
import android.webkit.WebViewClient;

import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.BridgeWebViewClient;
import com.yjy.superbridge.jsbridge.CallBackFunction;
import com.yjy.superbridge.jsbridge.JsBridgeCore;

import java.util.HashMap;
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
public class Bridge implements IBridgeCore{
    private IBridgeCore mCore;

    private Bridge(IBridgeCore core){
        this.mCore = core;
    }

    @Override
    public void registerHandler(String handlerName, BridgeHandler handler) {
        if(mCore == null){
            return;
        }
        mCore.registerHandler(handlerName,handler);
    }

    @Override
    public void unregisterHandler(String handlerName) {
        if(mCore == null){
            return;
        }
        mCore.unregisterHandler(handlerName);
    }

    @Override
    public void callHandler(String handlerName, String data, CallBackFunction callBack) {
        if(mCore == null){
            return;
        }
        mCore.callHandler(handlerName,data,callBack);
    }


    public void setCore(IBridgeCore mCore) {
        this.mCore = mCore;
    }

    public static class Builder{
        private IWebView webView;
        Map<String,Object> observableMap = new HashMap<>();
        WebViewClient client;
        IBridgeCore core;
        public Builder(IWebView webView){
            this.webView = webView;
        }



        public Builder registerInterface(String name,Object object){
            observableMap.put(name,object);
            return this;
        }

        public Builder setClientAndCore(IBridgeCore core,WebViewClient client){
            this.client = client;
            this.core = core;
            return this;
        }


        public Bridge build(){
            if(core == null||client == null){
                core = new JsBridgeCore(webView);
                client = new BridgeWebViewClient((JsBridgeCore)core);
            }

            for(Map.Entry<String,Object> entry : observableMap.entrySet()){
                BridgeHelper.registerInLow(entry.getValue(),core);
            }

            webView.setWebViewClient(client);

            return new Bridge(core);

        }

        public Bridge build(IBuilder builder){
            if(builder == null){
                return null;
            }

            if(core == null||client == null){
                core = new JsBridgeCore(webView);
                client = new BridgeWebViewClient((JsBridgeCore)core);
            }

            for(Map.Entry<String,Object> entry : observableMap.entrySet()){
                if(builder != null){
                    builder.build(entry.getKey(),entry.getValue(),core);
                }
            }

            webView.setWebViewClient(client);

            return new Bridge(core);

        }



    }


    public interface IBuilder{
        void build(String name,Object obj,IBridgeCore core);
    }
}
