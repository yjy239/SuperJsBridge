package com.yjy.superbridge.internal.model;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ResponseData<T> {
    private String handlerName;
    private T data;

    public T getData() {
        return data;
    }

    public ResponseData(String handlerName, T data) {
        this.handlerName = handlerName;
        this.data = data;
    }
}
