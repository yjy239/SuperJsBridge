package com.yjy.rnbridge.RnBridge.DefaultCompent;

import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.module.model.ReactModuleInfo;
import com.yjy.rnbridge.RnCompent.RnCore;

import javax.inject.Provider;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/31
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DefaultBridgeModule extends ModuleHolder {
    public DefaultBridgeModule(ReactModuleInfo moduleInfo, Provider<? extends NativeModule> provider) {
        super(moduleInfo, provider);
    }

    public DefaultBridgeModule(NativeModule nativeModule) {
        super(nativeModule);
    }

    @Override
    public String getName() {
        return RnCore.DEFAULT;
    }
}
