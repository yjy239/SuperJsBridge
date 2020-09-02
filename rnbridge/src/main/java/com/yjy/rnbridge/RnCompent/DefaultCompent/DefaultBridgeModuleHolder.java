package com.yjy.rnbridge.RnCompent.DefaultCompent;

import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.module.model.ReactModuleInfo;
import com.yjy.rnbridge.RnBridge.RnCore;

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
public class DefaultBridgeModuleHolder extends ModuleHolder {
    public DefaultBridgeModuleHolder(ReactModuleInfo moduleInfo, Provider<? extends NativeModule> provider) {
        super(moduleInfo, provider);
    }

    public DefaultBridgeModuleHolder(NativeModule nativeModule) {
        super(nativeModule);
    }

    @Override
    public String getName() {
        return RnCore.DEFAULT;
    }
}
