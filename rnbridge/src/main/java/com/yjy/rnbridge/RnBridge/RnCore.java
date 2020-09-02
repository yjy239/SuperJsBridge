package com.yjy.rnbridge.RnBridge;

import androidx.collection.ArrayMap;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.CatalystInstanceImpl;
import com.facebook.react.bridge.JavaModuleWrapper;
import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.NativeModuleRegistry;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.module.model.ReactModuleInfo;
import com.yjy.rnbridge.RnCompent.DefaultCompent.DefaultBridgeModuleHolder;
import com.yjy.rnbridge.RnCompent.DefaultCompent.DefaultBridgeModuleProvider;
import com.yjy.rnbridge.RnCompent.DefaultCompent.DefaultBridgeModuleWrapper;
import com.yjy.rnbridge.RnCompent.dynamic.BridgeJsModule;
import com.yjy.rnbridge.RnCompent.dynamic.BridgeModuleHolder;
import com.yjy.rnbridge.RnCompent.dynamic.BridgeModuleWrapper;
import com.yjy.rnbridge.RnCompent.dynamic.BridgeNativeModuleProvider;
import com.yjy.rnbridge.RnReceiveFromPlatformCallback;
import com.yjy.rnbridge.util.ReflectUtils;
import com.yjy.rnbridge.util.Utils;
import com.yjy.superbridge.internal.BaseBridgeCore;
import com.yjy.superbridge.internal.BridgeHelper;
import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.CallBackFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class RnCore extends BaseBridgeCore {
    private ReactInstanceManager mManager;
    private ReactContext mContext;
    private boolean isInit = false;
    private ArrayList<Runnable> mPendingList = new ArrayList<>();
    private CatalystInstance catalystInstance;
    private ArrayMap<String, BridgeModuleHolder> registryMap = new ArrayMap<>();
    private DefaultBridgeModuleWrapper moduleWrapper = null;
    private DefaultBridgeModuleHolder mDefaultHolder;
    public static final String DEFAULT = "default";

    private BridgeJsModule mJsModule = null;

    public RnCore(ReactInstanceManager manager){
        mManager = manager;
        mContext = mManager.getCurrentReactContext();
        if(mContext == null){
            mManager.addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
                @Override
                public void onReactContextInitialized(ReactContext context) {
                    mContext = context;
                    catalystInstance = mContext.getCatalystInstance();
                    isInit = true;

                    catalystInstance = mContext.getCatalystInstance();
                    mDefaultHolder = new DefaultBridgeModuleHolder(Utils.getInfo(DEFAULT,new ReactModuleInfo(DEFAULT,
                            DEFAULT,
                            false,true,
                            true,
                            false,false)),
                            new DefaultBridgeModuleProvider());
                    moduleWrapper = new DefaultBridgeModuleWrapper(catalystInstance,mDefaultHolder,RnCore.this);
                    mJsModule = new BridgeJsModule(catalystInstance);
                    for(int i = 0;i<mPendingList.size();i++){
                        mPendingList.get(i).run();
                    }

                    mPendingList.clear();

                    injectData();
                }
            });
        }else{
            catalystInstance = mContext.getCatalystInstance();
            mDefaultHolder = new DefaultBridgeModuleHolder(Utils.getInfo(DEFAULT,new ReactModuleInfo(DEFAULT,
                    DEFAULT,
                    false,true,
                    true,
                    false,false)),
                    new DefaultBridgeModuleProvider());
            moduleWrapper = new DefaultBridgeModuleWrapper(catalystInstance,mDefaultHolder,this);
            mJsModule = new BridgeJsModule(catalystInstance);
            isInit = true;
        }
    }

    @Override
    public void registerHandler(final String handlerName, final BridgeHandler handler,final boolean isInterceptor) {
        if(!isInit){
            mPendingList.add(new Runnable() {
                @Override
                public void run() {
                    moduleWrapper.registerMethod(handlerName,handler,isInterceptor);
                }
            });
        }else{
            moduleWrapper.registerMethod(handlerName,handler,isInterceptor);
        }
    }



    @Override
    public void unregisterHandler(final String handlerName) {
        if(!isInit){
            mPendingList.add(new Runnable() {
                @Override
                public void run() {
                    moduleWrapper.removeMethod(handlerName);
                }
            });
        }else{
            moduleWrapper.removeMethod(handlerName);
        }
    }

    @Override
    public void callHandler(final String handlerName, final String data, CallBackFunction callBack, boolean isInterceptor) {
        if(!isInit){
            mPendingList.add(new Runnable() {
                @Override
                public void run() {
                    Object[] args = {handlerName,data,null};
                    if(BridgeHelper.iterSendInterceptor(RnCore.this,args)){
                        return;
                    }
                    mJsModule.calHandler(handlerName,data);
                }
            });
        }else{
            Object[] args = {handlerName,data,null};
            if(BridgeHelper.iterSendInterceptor(RnCore.this,args)){
                return;
            }
            mJsModule.calHandler(handlerName,data);
        }
    }

    @Override
    public void registerObj(final String name, final Object obj) {
        if(!isInit){
            mPendingList.add(new Runnable() {
                @Override
                public void run() {
                    registerObjInternal(name, obj);
                }
            });
        }else{
            registerObjInternal(name, obj);
            injectData();
        }
    }

    private void registerObjInternal(String name, Object obj){
        if(catalystInstance!=null){

            registryMap.put(name,new BridgeModuleHolder(Utils.getInfo(name,obj),
                    new BridgeNativeModuleProvider(catalystInstance,this,
                            name,obj),name,this,obj));
        }
    }

    private void injectData(){
        ArrayList<JavaModuleWrapper> javaModules = new ArrayList<>();
        ArrayList<ModuleHolder> cxxModule = new ArrayList<>();

        NativeModuleRegistry registry =
                ReflectUtils.getNativeModuleRegistry((CatalystInstanceImpl) catalystInstance);
        HashMap<String,ModuleHolder> moduleMap = ReflectUtils.getModuleMap(registry);

        for (Map.Entry<String, ModuleHolder> entry : moduleMap.entrySet()) {
            if (!entry.getValue().isCxxModule()) {
                javaModules.add(new JavaModuleWrapper(catalystInstance, entry.getValue()));
            }else{
                cxxModule.add(entry.getValue());
            }
        }

        for(Map.Entry<String, BridgeModuleHolder> entry : registryMap.entrySet()){
            if (!entry.getValue().isCxxModule()) {
                javaModules.add(new BridgeModuleWrapper(catalystInstance, entry.getValue(),entry.getKey(),
                        this,entry.getValue().getRealObject()));
            }else{
                cxxModule.add(entry.getValue());
            }
        }

        javaModules.add(moduleWrapper);

        for (Map.Entry<String, ModuleHolder> entry : moduleMap.entrySet()) {
            String key = entry.getKey();
            if (!moduleMap.containsKey(key)) {
                ModuleHolder value = entry.getValue();
                moduleMap.put(key, value);
            }
        }

        ReflectUtils.registerJavaModule((CatalystInstanceImpl)catalystInstance,javaModules,cxxModule);
    }

    @Override
    public void release() {

    }

    @Override
    public RnReceiveFromPlatformCallback getReceiveCallback() {
        if(!(mReceiveFromPlatformCallback instanceof RnReceiveFromPlatformCallback)){
            return null;
        }
        return (RnReceiveFromPlatformCallback)mReceiveFromPlatformCallback;
    }
}
