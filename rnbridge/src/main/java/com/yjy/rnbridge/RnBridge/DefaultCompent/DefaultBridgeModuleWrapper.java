package com.yjy.rnbridge.RnBridge.DefaultCompent;

import android.text.TextUtils;

import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.bridge.BaseJavaModule;
import com.facebook.react.bridge.JSInstance;
import com.facebook.react.bridge.JavaModuleWrapper;
import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReadableNativeArray;
import com.yjy.rnbridge.RnCompent.RnCore;
import com.yjy.rnbridge.RnBridge.core.BridgeMethodWrapper;
import com.yjy.rnbridge.util.ReflectUtils;
import com.yjy.superbridge.internal.CallBackHandler;
import com.yjy.superbridge.internal.ProxyHandler;
import com.yjy.superbridge.jsbridge.BridgeHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/31
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DefaultBridgeModuleWrapper extends JavaModuleWrapper {
    private final ArrayList<MethodDescriptor> mDescs;
    private final ArrayList<NativeModule.NativeMethod> mMethods;
    private final Map<String,BridgeHandler> handlers = new HashMap();
    private final Map<String,MethodDescriptor>mDescsMap = new HashMap<>();
    private final Map<String,NativeModule.NativeMethod>mMethodsMap = new HashMap<>();

    //private final Map<String,PromiseBridgeHandler> promiseHandlers = new HashMap();
    private RnCore mCore;
    private final JSInstance mJSInstance;

    public DefaultBridgeModuleWrapper(JSInstance jsInstance, ModuleHolder moduleHolder,RnCore core) {
        super(jsInstance, moduleHolder);
        mDescs = new ArrayList<>();
        mMethods = new ArrayList<>();
        mCore = core;
        mJSInstance = jsInstance;
    }

    @Override
    public BaseJavaModule getModule() {
        return super.getModule();
    }

    @DoNotStrip
    public List<MethodDescriptor> getMethodDescriptors() {
        return mDescs;
    }

    public void registerMethod(String methodName, BridgeHandler handler,boolean isInterceptor){
        //这种方式也只能是异步
        if(!TextUtils.isEmpty(methodName)&&handler!=null){
            handlers.put(methodName,handler);
        }
        BridgeHandler obj = handler;
        if(isInterceptor){
            obj = new ProxyHandler(mCore,methodName,handler);
        }

        try {
            Method method =
                    BridgeHandler.class.getDeclaredMethod("handler",String.class,CallBackHandler.class);
            BridgeMethodWrapper wrapper = new BridgeMethodWrapper(this,
                    method,false,obj,mCore.getMethodMap().getConvertFactory());
            MethodDescriptor des = setMethodDescriptor(method,wrapper.getSignature(),methodName,wrapper.getType());
            mDescs.add(des);
            mDescsMap.put(methodName,des);
            mMethods.add(wrapper);
            mMethodsMap.put(methodName,wrapper);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeMethod(String methodName){
        //这种方式也只能是异步
        handlers.remove(methodName);
        MethodDescriptor des = mDescsMap.remove(methodName);
        mDescs.remove(des);
        NativeModule.NativeMethod method = mMethodsMap.remove(methodName);
        mMethods.remove(method);

    }

//    public void registerMethod(String methodName, PromiseBridgeHandler handler,boolean isInterceptor){
//        //这种方式也只能是异步
//        if(!TextUtils.isEmpty(methodName)&&handler!=null){
//            promiseHandlers.put(methodName,handler);
//        }
//
//        BridgeHandler obj = handler;
//        if(isInterceptor){
//            obj = new ProxyHandler(mCore,methodName,handler);
//        }
//
//        try {
//            Method method =
//                    BridgeHandler.class.getDeclaredMethod("handler", String.class, CallBackHandler.class);
//            BridgeMethodWrapper wrapper = new BridgeMethodWrapper(this,
//                    method,false,obj,mCore.getMethodMap().getConvertFactory());
//            mDescs.add(setMethodDescriptor(method,wrapper.getSignature(),methodName,wrapper.getType()));
//            mMethods.add(wrapper);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }



    private MethodDescriptor setMethodDescriptor(Method method, String signature, String name, String type){
        MethodDescriptor methodDescriptor = new MethodDescriptor();
        ReflectUtils.setField(methodDescriptor,"method",method);
        ReflectUtils.setField(methodDescriptor,"signature",signature);
        ReflectUtils.setField(methodDescriptor,"name",name);
        ReflectUtils.setField(methodDescriptor,"type",type);
        return methodDescriptor;
    }

    @Override
    public void invoke(int methodId, ReadableNativeArray parameters) {
        if (mMethods == null || methodId >= mMethods.size()) {
            return;
        }

        mMethods.get(methodId).invoke(mJSInstance, parameters);
    }
}
