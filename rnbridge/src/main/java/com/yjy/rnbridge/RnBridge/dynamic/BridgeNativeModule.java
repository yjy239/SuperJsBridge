package com.yjy.rnbridge.RnBridge.dynamic;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.BaseJavaModule;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BridgeNativeModule extends BaseJavaModule {
    String mName;
    public BridgeNativeModule(String name) {
        this.mName = name;
    }

    @NonNull
    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void initialize() {

    }

    @Override
    public boolean canOverrideExistingModule() {
        return false;
    }

    @Override
    public void onCatalystInstanceDestroy() {

    }
}
