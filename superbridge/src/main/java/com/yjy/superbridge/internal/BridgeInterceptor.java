package com.yjy.superbridge.internal;

import com.yjy.superbridge.internal.model.ResponseData;

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

    boolean receiverInterceptor(String handlerName, D data);

    boolean sendInterceptor(String handlerName, Object data);
}
