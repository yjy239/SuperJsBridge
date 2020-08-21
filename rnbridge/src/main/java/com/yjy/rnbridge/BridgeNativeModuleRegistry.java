package com.yjy.rnbridge;

import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.NativeModuleRegistry;
import com.facebook.react.bridge.ReactApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BridgeNativeModuleRegistry extends NativeModuleRegistry {
    private Object object;
    public BridgeNativeModuleRegistry(ReactApplicationContext reactApplicationContext,HashMap<String, ModuleHolder> map) {
        super(reactApplicationContext, map);
    }
}
