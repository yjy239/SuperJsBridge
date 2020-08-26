package com.yjy.rnbridge.RnBridge;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TransformObject<T> {
    public T NativeMap;

    public T getNativeMap() {
        return NativeMap;
    }

    public void setNativeMap(T nativeMap) {
        NativeMap = nativeMap;
    }
}
