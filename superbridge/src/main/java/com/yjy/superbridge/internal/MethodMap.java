package com.yjy.superbridge.internal;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.yjy.superbridge.internal.convert.ConvertFactory;
import com.yjy.superbridge.internal.convert.Converter;
import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.CallBackFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/04
 *     desc   : 所有Bridge的方法表
 *     version: 1.0
 * </pre>
 */
public class MethodMap {
    Map<Object,Map<String,Invoker>> methodMap = new HashMap<>();
    private Map<String, Object> mNamespaceInterfaces = new HashMap<String, Object>();
    Map<String, BridgeHandler> handlerMap = new HashMap<>();
    private ConvertFactory mConvertFactory;


    public void put(String namespace,Method method,Object obj){
        Map<String,Invoker> map = methodMap.get(obj);
        if(map==null){
            map = new HashMap<>();
        }

        //这么做是为了校验必须存在BridgeMethod注解
        map.put(method.getName(),new Invoker(method));

        methodMap.put(obj, map);
        mNamespaceInterfaces.put(namespace !=null?namespace:"",obj);

    }

    public boolean hasMethod(String name){
        String[] nameSlits = parseNamespace(name);
        String method = nameSlits[1];
        return hasMethod(nameSlits[0],method);
    }

    public boolean hasMethod(String namespace,String methodName){

        Object object = mNamespaceInterfaces.get(namespace);
        if(object == null){
            throw new IllegalArgumentException("not register namespace");
        }


        Map<String,Invoker> map = methodMap.get(object);
        return map!=null&&map.get(methodName) != null;

    }

    public void put(String name,BridgeHandler method){

        handlerMap.put(name, method);
    }

    public void remove(String name){
        handlerMap.remove(name);
    }

    public void remove(String namespace,String name){
        Object obj = mNamespaceInterfaces.get(namespace);
        if(obj == null){
            return;
        }
        Map<String,Invoker> map = methodMap.get(obj);
        if(map==null){
            return;
        }
        map.remove(name);

    }

    public static String[] parseNamespace(String method) {
        int pos = method.lastIndexOf('.');
        String namespace = "";
        if (pos != -1) {
            namespace = method.substring(0, pos);
            method = method.substring(pos + 1);
        }
        return new String[]{namespace, method};
    }

    public boolean isAsync(String name){
        String[] split = parseNamespace(name);
        return isAsync(split[0],split[1]);
    }

    public boolean isAsync(String namespace,String name){
        Object obj = mNamespaceInterfaces.get(namespace);
        if(obj == null){
            return false;
        }
        Map<String,Invoker> map = methodMap.get(obj);
        if(map == null){
            return false;
        }

        Invoker method = map.get(name);
        if(method ==null){
            return false;
        }
        return method.isAsync();
    }

    public boolean isInterceptor(String name){
        String[] split = parseNamespace(name);
        return isInterceptor(split[0],split[1]);
    }


    public boolean isInterceptor(String namespace,String name){
        Object obj = mNamespaceInterfaces.get(namespace);
        if(obj == null){
            return false;
        }
        Map<String,Invoker> map = methodMap.get(obj);
        Invoker method = map.get(name);

        return method.isInterceptor();
    }


    public Object invokeWithFields(String name, String args, CallBackHandler handler){
        String[] nameSlits = parseNamespace(name);
        String method = nameSlits[1];

        Object object = mNamespaceInterfaces.get(nameSlits[0]);
        if(object == null){
            throw new IllegalArgumentException("not register namespace");
        }


        Map<String,Invoker> map = methodMap.get(object);
       if(map != null){
           Invoker invoker = map.get(method);
           if(invoker != null){
               return invoker.invokeWithField(object,args,handler,mConvertFactory);
           }

       }else {
           BridgeHandler bridge = handlerMap.get(method);
           if(bridge != null){
               bridge.handler(args,handler);
           }

           return null;
       }

       return null;

    }




    public void setConvertFactory(ConvertFactory factory) {
        mConvertFactory = factory;
    }

    public ArrayList<Invoker> get(String  namespace) {
        ArrayList<Invoker> methods = new ArrayList<>();
        Object obj = mNamespaceInterfaces.get(namespace);
        if(obj == null){
            return null;
        }
        Map<String,Invoker> map = methodMap.get(obj);
        for(Map.Entry<String,Invoker> invokerEntry : map.entrySet()){
            methods.add(invokerEntry.getValue());
        }
        return methods;

    }

    public static class Invoker{
        private Method method = null;
        private int isAsync = ASYNC_UNDEFINED;
        private int isInterceptor= INTERCEPTOR_UNDEFINED;

        static final int ASYNC_UNDEFINED = 0;
        static final int ASYNC =1;
        static final int SYNC = 2;

        static final int INTERCEPTOR_UNDEFINED = 0;
        static final int INTERCEPTOR =1;
        static final int DISPATCH = 2;


        Invoker(Method method) {
            this.method = method;
        }

        Object invoke(Object obj,Object... args){
            try {
                method.setAccessible(true);
                return method.invoke(obj,args);
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        public Method getMethod() {
            return method;
        }

        public boolean isAsync() {
            if(isAsync == ASYNC_UNDEFINED){
                boolean async = false;
                Class<?>[] types = method.getParameterTypes();
                if(types == null||types.length == 0){
                    isAsync = SYNC;
                }else{
                    async =  types[types.length-1] == CallBackHandler.class
                            ||CallBackHandler.class.isAssignableFrom(types[types.length-1]);

                    isAsync = async?ASYNC:SYNC;
                }

            }
            return isAsync == ASYNC;
        }

        boolean isInterceptor(){
            if(isInterceptor == INTERCEPTOR_UNDEFINED){
                BridgeMethod annotation = method.getAnnotation(BridgeMethod.class);
                if(annotation == null){
                    return false;
                }
                boolean interceptor =  annotation.interceptor();
                isInterceptor = interceptor?INTERCEPTOR:DISPATCH;
            }

            return isInterceptor == INTERCEPTOR;
        }


        //直接注入
        Object invokeWithField(Object obj,String data,CallBackHandler handler,ConvertFactory factory){
            try {
                if(data == null){
                    invoke(obj,data,handler);
                    return null;
                }

                Annotation[][] annotations = method.getParameterAnnotations();
                Class<?>[] types = method.getParameterTypes();

                ArrayList<String> methodParameterNames = new ArrayList<String>(types.length);
                for (int i = 0; i < annotations.length; i++) {
                    for (int j = 0; j < annotations[i].length; j++) {
                        if (annotations[i][j].annotationType() != BridgeField.class) {
                            continue;
                        }

                        BridgeField field = (BridgeField) annotations[i][j];
                        String name = field.name();
                        methodParameterNames.add(name);
                    }
                }

                boolean isAsync = types[types.length-1] == CallBackHandler.class
                        ||CallBackHandler.class.isAssignableFrom(types[types.length-1]);

                if(methodParameterNames.size() == 0){
                    try {
                        if(isAsync){
                            invoke(obj,data,handler);
                            return null;
                        }else {
                            return invoke(obj,data);
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                //根据类型转换数据到数组
                Object[] params = new Object[types.length];


                try {
                    JSONObject object = new JSONObject(data);

                    for (int i = 0; i < methodParameterNames.size(); i++) {
                        transform(params, i, object.getString(methodParameterNames.get(i)), types[i],factory);
                    }
                    if(isAsync){
                        params[types.length - 1] = handler;
                    }


                    return invoke(obj,params);
                }catch (JSONException je){
                    je.printStackTrace();
                    return invoke(obj,params);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;

        }

        private void transform(Object[] params, int index, String value, Type type,ConvertFactory factory) {
            if (type == int.class||type == Integer.class) {
                params[index] = StrUtil.toInt(value);
            } else if (type == boolean.class||type == Boolean.class) {
                params[index] = StrUtil.toBool(value);
            } else if (type == double.class||type == Double.class) {
                params[index] = StrUtil.toDouble(value);
            } else if (type == float.class||type == Float.class) {
                params[index] = StrUtil.toFloat(value);
            } else if (type == short.class||type == Short.class) {
                params[index] = StrUtil.toShort(value);
            } else if (type == byte.class||type == Byte.class) {
                params[index] = StrUtil.toByte(value);
            } else if (type == long.class||type == Long.class) {
                params[index] = StrUtil.toLong(value);
            }else if (type == String.class) {
                params[index] = value;
            }else if (type == Uri.class) {
                params[index] = TextUtils.isEmpty(value)?null:Uri.parse(value);
            } else {
                if(factory != null){
                    Converter converter = factory.createConverter(type);
                    if(converter != null){
                        try {
                            params[index] = converter.convert(value);
                        }catch (Exception e){
                            e.printStackTrace();
                            params[index] = null;
                        }
                    }else {
                        params[index] = null;
                    }

                }else {
                    params[index] = null;
                }

            }
        }
    }
}
