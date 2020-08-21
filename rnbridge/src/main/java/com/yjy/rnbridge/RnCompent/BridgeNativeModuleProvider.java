package com.yjy.rnbridge.RnCompent;

import com.facebook.react.bridge.JSInstance;
import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.module.model.ReactModuleInfo;
import com.yjy.rnbridge.BridgeModuleWrapper;
import com.yjy.rnbridge.BridgeNativeModule;
import com.yjy.rnbridge.RnCore;

import javax.inject.Provider;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BridgeNativeModuleProvider implements Provider<NativeModule> {
    JSInstance mJsInstance;
    RnCore mCore;
    String mName;
    Object mObj;
    private ModuleHolder moduleHolder;
    public BridgeNativeModuleProvider(JSInstance jsInstance, RnCore core,String name, Object obj) {
        mJsInstance = jsInstance;
        mCore = core;
        mName = name;
        mObj = obj;
    }

    @Override
    public NativeModule get() {
        return new BridgeNativeModule(mName);
    }

}
