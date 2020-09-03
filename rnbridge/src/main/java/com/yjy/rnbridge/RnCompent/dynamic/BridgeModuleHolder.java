package com.yjy.rnbridge.RnCompent.dynamic;

import android.util.Log;

import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.module.model.ReactModuleInfo;
import com.yjy.superbridge.internal.IBridgeCore;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Logger;

import javax.inject.Provider;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BridgeModuleHolder extends ModuleHolder {
    private static final String TAG = BridgeModuleHolder.class.getSimpleName();
    private NativeModule nativeModule = null;
    private String mName;
    private IBridgeCore mCore;
    private Object mRealObject;

    public BridgeModuleHolder(ReactModuleInfo moduleInfo, Provider<? extends NativeModule> provider,
                              String name,IBridgeCore core,NativeModule object) {
        super(moduleInfo, provider);
        mName = name;
        mCore= core;
        nativeModule = object;
    }

    public BridgeModuleHolder(ReactModuleInfo moduleInfo, Provider<? extends NativeModule> provider,
                              String name,IBridgeCore core,Object object) {
        super(moduleInfo, provider);
        mName = name;
        mCore= core;
        mRealObject = object;
    }

    public BridgeModuleHolder(NativeModule nativeModule) {
        super(nativeModule);
    }

    @Override
    public NativeModule getModule() {
        return nativeModule;
    }

    @Override
    public String getName() {
        return mName;
    }

    public Object getRealObject() {
        return nativeModule !=null?nativeModule: mRealObject;
    }
}
