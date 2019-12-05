package com.yjy.superbridge.internal;

import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.CallBackFunction;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/11
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class BridgeHelper {



    public static void registerInLow(Object obj, IBridgeCore core){
        if(obj != null){
            Method[] methods = obj.getClass().getDeclaredMethods();
            for (int i = 0;i<methods.length;i++){
                final Method method = methods[i];
                int modify = method.getModifiers();
                Type type = method.getReturnType();
                Type[] types = method.getParameterTypes();
                if(modify == Modifier.PUBLIC&&types.length<=2){
                    if(type == void.class||type==String.class){
                        register(obj,type,method,core);
                    }else {
                        throw new IllegalArgumentException("return type must be String or Void");
                    }
                }
            }
        }

    }

    public static BridgeHandler getProxyHandler(final Object obj, final Type type, final Method method){
        BridgeHandler handler = null;


        if(type == void.class){
            handler = new BridgeHandler() {
                @Override
                public void handler(String data, CallBackFunction function) {
                    try {
                        Type[] types = method.getGenericParameterTypes();
                        if(types.length == 0){
                            method.invoke(obj);
                        }else if(types.length == 1&&types[0] == String.class){
                            method.invoke(obj,data);
                        }else if(types.length == 2
                                &&types[0] == String.class
                                &&types[1] == CallBackFunction.class){
                            method.invoke(obj,data,function);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            };

        }else if(type == String.class){
            handler = new BridgeHandler() {
                @Override
                public void handler(String data, CallBackFunction function) {
                    try {
                        String result = (String)method.invoke(obj,data);
                        function.onCallBack(result);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            };
        }

        return handler;

    }

    private static void register(final Object obj, final Type type, final Method method,
                                 IBridgeCore core){
        core.registerHandler(method.getName(), getProxyHandler(obj, type, method));
    }


    public static boolean iterSendInterceptor(IBridgeCore core,Object[] args){
        boolean isInterceptor = false;
        if(core != null){
            List<BridgeInterceptor> interceptors = core.getInterceptor();
            for(int i = 0;i<interceptors.size();i++){
                if(args.length == 3){
                    isInterceptor |= interceptors.get(i).sendInterceptor((String)args[0],(String)args[1],(CallBackFunction) args[2]);
                }else if(args.length == 2){
                    if(args[1] instanceof CallBackFunction){
                        isInterceptor |= interceptors.get(i).sendInterceptor((String)args[0],null,(CallBackFunction) args[1]);
                    }else {
                        isInterceptor |= interceptors.get(i).sendInterceptor((String)args[0],args[1].toString(),null );
                    }
                }else if(args.length == 1){
                    isInterceptor |= interceptors.get(i).sendInterceptor((String)args[0],null,null);
                }else if(args.length == 0){
                    isInterceptor |= interceptors.get(i).sendInterceptor(null,null,null);
                }

                if(isInterceptor){
                    return isInterceptor;
                }
            }
        }

        return isInterceptor;
    }


    public static boolean iterReceiveInterceptor(IBridgeCore core,String name,Object[] args){
        boolean isInterceptor = false;
        if(core != null){
            List<BridgeInterceptor> interceptors = core.getInterceptor();
            for(int i = 0;i<interceptors.size();i++){
                if(args.length == 2){
                    isInterceptor |= interceptors.get(i).receiverInterceptor(name, args[0], args[1]);
                }else if(args.length == 1){
                    isInterceptor |= interceptors.get(i).receiverInterceptor(name, args[0], null);
                }else if(args.length == 0){
                    isInterceptor |= interceptors.get(i).receiverInterceptor(name, null, null);
                }

                if(isInterceptor){
                    return isInterceptor;
                }
            }
        }

        return isInterceptor;
    }



}
