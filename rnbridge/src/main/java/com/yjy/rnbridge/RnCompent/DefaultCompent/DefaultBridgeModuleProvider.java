package com.yjy.rnbridge.RnCompent.DefaultCompent;

import com.facebook.react.bridge.NativeModule;
import com.yjy.rnbridge.RnBridge.RnCore;
import com.yjy.rnbridge.RnCompent.dynamic.BridgeNativeModule;

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
public class DefaultBridgeModuleProvider implements Provider<NativeModule> {

    @Override
    public NativeModule get() {
        return new BridgeNativeModule(RnCore.DEFAULT);
    }
}
