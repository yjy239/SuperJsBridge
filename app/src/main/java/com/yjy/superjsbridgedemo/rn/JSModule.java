package com.yjy.superjsbridgedemo.rn;

import com.facebook.react.bridge.JavaScriptModule;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/09/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface JSModule extends JavaScriptModule {
    void callHandler(String name);
}
