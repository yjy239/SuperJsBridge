package com.yjy.superbridge.internal;

import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.CallBackFunction;

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
    void callHandler(String handlerName, String data, CallBackFunction callBack);

}
