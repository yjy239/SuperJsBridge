package com.yjy.superbridge.internal;

import com.yjy.superbridge.jsbridge.BridgeHandler;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/12/05
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class ProxyHandler implements BridgeHandler {

    private BridgeHandler mInnerHandler;
    private IBridgeCore mCore;
    private String mName;

    public ProxyHandler(IBridgeCore core,String name,BridgeHandler innerHandler){
        this.mInnerHandler = innerHandler;
        this.mCore = core;
        this.mName = name;
    }

    @Override
    public void handler(String data, CallBackHandler<String> function) {
        if(mInnerHandler != null&&mCore!=null){
            Object[] args = {data,function};
            if(BridgeHelper.iterReceiveInterceptor(mCore,mName,args)){
                return;
            }
            mInnerHandler.handler(data,function);
        }
    }
}
