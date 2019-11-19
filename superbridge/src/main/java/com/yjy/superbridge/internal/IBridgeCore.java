package com.yjy.superbridge.internal;

import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.CallBackFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/11
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public interface IBridgeCore {
    void registerHandler(String handlerName, BridgeHandler handler);
    void unregisterHandler(String handlerName);

    @SendBridge
    void callHandler(String handlerName, String data, CallBackFunction callBack);

    void addInterceptor(BridgeInterceptor interceptor);

    void removeInterceptor(BridgeInterceptor interceptor);

    void setInterceptor(List<BridgeInterceptor> interceptors);

    List<BridgeInterceptor> getInterceptor();


    void release();

}
