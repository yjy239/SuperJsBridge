package com.yjy.rnbridge.util;

import com.facebook.react.bridge.CatalystInstanceImpl;
import com.facebook.react.bridge.JavaModuleWrapper;
import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.NativeModuleRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ReflectUtils {
    public static void setField(Object obj,String name,Object value){
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj,value);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void registerJavaModule(CatalystInstanceImpl impl,
                                          Collection<JavaModuleWrapper> javaModules,
                                          Collection<ModuleHolder> cxxModules){
        try {
            Method method = impl.getClass().getDeclaredMethod("jniExtendNativeModules",
                    Collection.class,Collection.class);
            method.setAccessible(true);
            method.invoke(impl,javaModules,cxxModules);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static NativeModuleRegistry getNativeModuleRegistry(CatalystInstanceImpl impl){
        NativeModuleRegistry registry = null;
        try {
            Field field = impl.getClass().getDeclaredField("mNativeModuleRegistry");
            field.setAccessible(true);
            return (NativeModuleRegistry)field.get(impl);
        }catch (Exception e){
            e.printStackTrace();
        }

        return registry;
    }

    public static HashMap<String,ModuleHolder> getModuleMap(NativeModuleRegistry registry){
        HashMap<String,ModuleHolder> map = new HashMap<>();

        try {
            Method method = registry.getClass().getDeclaredMethod("getModuleMap");
            method.setAccessible(true);
            map = (HashMap<String, ModuleHolder>) method.invoke(registry);
        }catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }
}
