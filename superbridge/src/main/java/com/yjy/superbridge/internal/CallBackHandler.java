package com.yjy.superbridge.internal;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface CallBackHandler<T> {
    void complete(T data);
    void complete();
    void setProgressData(T value);
}
