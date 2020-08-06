package com.yjy.superbridge.internal;

import com.yjy.superbridge.internal.model.ResponseData;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface ReceiveFromPlatformCallback<T> {
    ResponseData<T> onFound(ResponseData<T> data);
}
