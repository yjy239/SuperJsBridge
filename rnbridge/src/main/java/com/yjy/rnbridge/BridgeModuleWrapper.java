package com.yjy.rnbridge;

import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.bridge.BaseJavaModule;
import com.facebook.react.bridge.JSInstance;
import com.facebook.react.bridge.JavaModuleWrapper;
import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.systrace.Systrace;
import com.yjy.rnbridge.util.ReflectUtils;
import com.yjy.superbridge.internal.BridgeHelper;
import com.yjy.superbridge.internal.MethodMap;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.systrace.Systrace.TRACE_TAG_REACT_JAVA_BRIDGE;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BridgeModuleWrapper extends JavaModuleWrapper {
    private final ArrayList<MethodDescriptor> mDescs;
    private final ArrayList<NativeModule.NativeMethod> mMethods;
    private RnCore mCore;
    private String mName;
    private Object mActionObj;
    private JSInstance mJSInstance;
    public BridgeModuleWrapper(JSInstance jsInstance, ModuleHolder holder,
                               String name, RnCore core, Object object) {
        super(jsInstance, holder);
        mCore = core;
        mDescs = new ArrayList<>();
        mMethods = new ArrayList<>();
        mName = name;
        mActionObj = object;
        mJSInstance = jsInstance;
    }

    @Override
    public BaseJavaModule getModule() {
        return super.getModule();
    }

    @DoNotStrip
    public List<MethodDescriptor> getMethodDescriptors() {
        if (mDescs.isEmpty()) {
            findBridgeMethods();
        }
        return mDescs;
    }

    private void findBridgeMethods() {
        Systrace.beginSection(TRACE_TAG_REACT_JAVA_BRIDGE, "findMethods");
        BridgeHelper.registerInLow(mName,mActionObj,mCore,false);
        mDescs.addAll(convertMethodMapToDes());
        Systrace.endSection(TRACE_TAG_REACT_JAVA_BRIDGE);
    }

    private ArrayList<MethodDescriptor> convertMethodMapToDes(){
        ArrayList<MethodDescriptor> list = new ArrayList<>();
        MethodMap methodMap = mCore.getMethodMap();
        List<MethodMap.Invoker> methods  = methodMap.get(mName);

        for(int i = 0;i<methods.size();i++){
            boolean isSync = !methods.get(i).isAsync();
            Class<?>[] types = methods.get(i).getMethod().getParameterTypes();
            Class<?> returnTypes = methods.get(i).getMethod().getReturnType();
            boolean isPromise = false;
            if(types.length > 1){
                isPromise = (types[types.length-1] != Promise.class);
            }
            //void 就是async，不需要返回等待
            boolean  isVoid = (returnTypes == void.class);

            if(isPromise){
                isSync = false;
            }else if(isVoid){
                isSync = false;
            }

            BridgeMethodWrapper wrapper = new BridgeMethodWrapper(this,
                    methods.get(i).getMethod(),isSync,mActionObj,mCore.getMethodMap().getConvertFactory());
            list.add(setMethodDescriptor(wrapper.getMethod(),
                    wrapper.getSignature(),wrapper.getMethod().getName(),wrapper.getType()));
            mMethods.add(wrapper);
        }

        return list;
    }

    private MethodDescriptor setMethodDescriptor(Method method,String signature,String name,String type){
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
