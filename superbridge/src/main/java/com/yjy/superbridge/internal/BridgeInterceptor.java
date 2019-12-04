package com.yjy.superbridge.internal;

import com.yjy.superbridge.jsbridge.CallBackFunction;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/14
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public interface BridgeInterceptor<D,F> {

    boolean receiverInterceptor(String handlerName, D data, F function);

    boolean sendInterceptor(String handlerName, String data, CallBackFunction callBack);
}
