package com.yjy.superbridge.internal;

import com.yjy.superbridge.internal.convert.ConvertFactory;
import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.CallBackFunction;

import java.util.ArrayList;
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
        IBridgeClient client;
        IBridgeCore core;
        IBridgeFactory factory;
        ConvertFactory convertFactory;
        ArrayList<BridgeInterceptor> mInterceptors = new ArrayList<>();
        public Builder(IWebView webView){
            this.webView = webView;
        }



        public Builder registerInterface(String name,BridgeInterface object){
            observableMap.put(name,object);
            return this;
        }

        public Builder setClientFactory(IBridgeFactory factory){
            this.factory =factory;
            return this;
        }

        public Builder setConvertFactory(ConvertFactory factory){
            this.convertFactory =factory;
            return this;
        }

        public Builder addInterceptor(BridgeInterceptor interceptor){
            mInterceptors.add(interceptor);
            return this;
        }


        public Bridge build(){
            if(factory == null){
                throw new IllegalArgumentException("must be set Factory");
            }

            core = factory.getBridgeCore();
            client = factory.getBridgeClient();

            core.setInterceptor(mInterceptors);

            core.setConvertFactory(convertFactory);

            core.setReceiveCallback(factory.getReceiveFromPlatformCallback());

            for(Map.Entry<String,BridgeInterface> entry : observableMap.entrySet()){
                core.registerObj(entry.getKey(),entry.getValue());
            }

            webView.setClient(client);

            return new Bridge(core);
        }


    }


}
