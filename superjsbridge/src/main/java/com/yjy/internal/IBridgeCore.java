package com.yjy.internal;

import com.yjy.superjsbridgedemo.jsbridge.BridgeHandler;
import com.yjy.superjsbridgedemo.jsbridge.CallBackFunction;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/08
 *     desc   : js Bridge的核心方法
 *     github:yjy239@gitub.com
 * </pre>
 */
public interface IBridgeCore {


    /**
     * 注册方法给js
     * @param handlerName
     * @param handler
     */
    void registerHandler(String handlerName, BridgeHandler handler);


    /**
     * 注销方法
     * @param handlerName
     */
    void unregisterHandler(String handlerName);


    /**
     * call javascript registered handler
     * 调用javascript处理程序注册
     * @param handlerName handlerName
     * @param data data
     * @param callBack CallBackFunction
     */
    void callHandler(String handlerName, String data, CallBackFunction callBack);



}
