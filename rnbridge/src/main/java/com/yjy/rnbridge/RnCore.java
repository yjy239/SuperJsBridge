package com.yjy.rnbridge;

import androidx.collection.ArrayMap;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.CatalystInstanceImpl;
import com.facebook.react.bridge.JavaModuleWrapper;
import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.NativeModuleRegistry;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.yjy.rnbridge.RnCompent.BridgeNativeModuleProvider;
import com.yjy.rnbridge.util.ReflectUtils;
import com.yjy.rnbridge.util.Utils;
import com.yjy.superbridge.internal.BaseBridgeCore;
import com.yjy.superbridge.internal.ReceiveFromPlatformCallback;
import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.CallBackFunction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
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
    private ArrayMap<String,BridgeModuleHolder> registryMap = new ArrayMap<>();

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

                    for(int i = 0;i<mPendingList.size();i++){
                        mPendingList.get(i).run();
                    }

                    mPendingList.clear();

                    injectData();
                }
            });
        }else{
            catalystInstance = mContext.getCatalystInstance();
            isInit = true;
        }
    }

    @Override
    public void registerHandler(String handlerName, BridgeHandler handler, boolean isInterceptor) {
        if(!isInit){
            mPendingList.add(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }



    @Override
    public void unregisterHandler(String handlerName) {

    }

    @Override
    public void callHandler(String handlerName, String data, CallBackFunction callBack, boolean isInterceptor) {

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
    public <T extends ReceiveFromPlatformCallback> T getReceiveCallback() {
        return null;
    }
}
