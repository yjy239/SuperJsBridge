package com.yjy.superbridge.internal;

import com.yjy.superbridge.internal.convert.ConvertFactory;
import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.CallBackFunction;
import com.yjy.superbridge.jsbridge.JSReceiveFromPlatformCallback;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/15
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public abstract class BaseBridgeCore implements IBridgeCore{

    private List<BridgeInterceptor> mInterceptors = new ArrayList<>();
    private MethodMap methodMap = new MethodMap();


    @Override
    public void addInterceptor(BridgeInterceptor interceptor) {
        mInterceptors.add(interceptor);
    }

    @Override
    public void removeInterceptor(BridgeInterceptor interceptor) {
        mInterceptors.remove(interceptor);
    }

    @Override
    public void setInterceptor(List<BridgeInterceptor> interceptors) {
        mInterceptors.addAll(interceptors);
    }

    @Override
    public List<BridgeInterceptor> getInterceptor() {
        return mInterceptors;
    }

    @Override
    public void register(String namespace, String name, Method method, Object obj) {
        methodMap.put(namespace,method,obj);
    }


    @Override
    public void registerHandler(String handlerName, BridgeHandler handler) {
        registerHandler(handlerName, handler,false);
    }

    @Override
    public void register(String name, BridgeHandler method) {
        methodMap.put(name,method);
    }

    @Override
    public void unregister(String name) {
        methodMap.remove(name);
    }

    @Override
    public void unregister(String namespace, String name) {
        methodMap.remove(namespace,name);
    }

    @Override
    public void callHandler(String handlerName, String data, CallBackFunction callBack) {
        callHandler(handlerName, data, callBack,false);
    }

    @Override
    public MethodMap getMethodMap() {
        return methodMap;
    }

    @Override
    public void setConvertFactory(ConvertFactory factory) {
        methodMap.setConvertFactory(factory);
    }

    protected ReceiveFromPlatformCallback mReceiveFromPlatformCallback;

    @Override
    public void setReceiveCallback(ReceiveFromPlatformCallback callback) {
        mReceiveFromPlatformCallback = callback;
    }

}
