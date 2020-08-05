package com.yjy.superbridge.internal;

import android.content.Context;
import android.net.Uri;
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
import java.util.Map;


/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MethodMap {
    Map<Object,Map<String,Method>> methodMap = new HashMap<>();
    private Map<String, Object> mNamespaceInterfaces = new HashMap<String, Object>();
    Map<String, BridgeHandler> handlerMap = new HashMap<>();
    private ConvertFactory mConvertFactory;



    public void put(String namespace,Method method,Object obj){
        Map<String,Method> map = methodMap.get(obj);
        if(map==null){
            map = new HashMap<>();
        }

        //这么做是为了校验必须存在BridgeMethod注解
        map.put(method.getName(),method);

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


        Map<String,Method> map = methodMap.get(object);
        return map.get(methodName) != null;

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
        Map<String,Method> map = methodMap.get(obj);
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
        Map<String,Method> map = methodMap.get(obj);
        if(map == null){
            return false;
        }

        Method method = map.get(name);
        if(method ==null){
            return false;
        }
        Class<?>[] types = method.getParameterTypes();
        if(types == null||types.length == 0){
            return false;
        }
        return types[types.length-1] == CallBackHandler.class
                ||CallBackHandler.class.isAssignableFrom(types[types.length-1]);
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
        Map<String,Method> map = methodMap.get(obj);
        Method method = map.get(name);
        BridgeMethod annotation = method.getAnnotation(BridgeMethod.class);
        if(annotation == null){
            return false;
        }
        return annotation.interceptor();
    }


    public Object invokeWithFields(String name, String args, CallBackHandler handler){
        String[] nameSlits = parseNamespace(name);
        String method = nameSlits[1];

        Object object = mNamespaceInterfaces.get(nameSlits[0]);
        if(object == null){
            throw new IllegalArgumentException("not register namespace");
        }


        Map<String,Method> map = methodMap.get(object);
       if(map != null){
           Method invoker = map.get(method);
           if(invoker != null){
               return invokeWithField(object,invoker,args,handler);
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


    //直接注入
    public Object invokeWithField(Object obj,Method method,String data,CallBackHandler handler){
        try {
            if(data == null){
                return invoke(obj,method,data,handler);
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
                        invoke(obj,method,data,handler);
                        return null;
                    }else {
                        return invoke(obj,method,data);
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
                    transform(params, i, object.getString(methodParameterNames.get(i)), types[i]);
                }
                if(isAsync){
                    params[types.length - 1] = handler;
                }


                return invoke(obj,method,params);
            }catch (JSONException je){
                je.printStackTrace();
                return invoke(obj,method,params);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    private void transform(Object[] params, int index, String value, Type type) {
        if (type == int.class) {
            params[index] = StrUtil.toInt(value);
        } else if (type == boolean.class) {
            params[index] = StrUtil.toBool(value);
        } else if (type == double.class) {
            params[index] = StrUtil.toDouble(value);
        } else if (type == float.class) {
            params[index] = Float.parseFloat(value);
        } else if (type == short.class) {
            params[index] = Short.parseShort(value);
        } else if (type == byte.class) {
            params[index] = Byte.parseByte(value);
        } else if (type == long.class) {
            params[index] = Long.parseLong(value);
        } else if (type == String.class) {
            params[index] = value;
        }else if (type == Uri.class) {
            params[index] = Uri.parse(value);
        } else {
            if(mConvertFactory != null){
                Converter converter = mConvertFactory.createConverter(type);
                if(converter != null){
                    try {
                        params[index] = converter.convert(value);
                    }catch (Exception e){
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

    public Object invoke(Object obj,Method method,Object... args){
        try {
            method.setAccessible(true);
            return method.invoke(obj,args);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void setConvertFactory(ConvertFactory factory) {
        mConvertFactory = factory;
    }

    public static class Invoker{
        Method method;
        boolean isAsync;

        public Invoker(Method method, boolean isAsync) {
            this.method = method;
            this.isAsync = isAsync;
        }

        public Object invoke(Object obj,Method method,Object... args){
            try {
                method.setAccessible(true);
                return method.invoke(obj,args);
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        public boolean isAsync() {
            return isAsync;
        }
    }
}
