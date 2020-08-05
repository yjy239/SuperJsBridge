package com.yjy.superbridge.internal;

import com.yjy.superbridge.jsbridge.BridgeHandler;

import java.lang.reflect.Method;
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



    public static void registerInLow(String namespace,Object obj, IBridgeCore core,boolean registerHandler){
        if(obj != null){
            Method[] methods = obj.getClass().getDeclaredMethods();
            for (int i = 0;i<methods.length;i++){
                final Method method = methods[i];
                int modify = method.getModifiers();
                Type[] types = method.getParameterTypes();
                BridgeMethod bridgeMethod = method.getAnnotation(BridgeMethod.class);
                if(bridgeMethod != null){
                    register(namespace,obj,method,core,bridgeMethod.interceptor(),registerHandler);
                }

            }
        }

    }

    private static BridgeHandler getProxyHandler(final String name, final IBridgeCore core){
        BridgeHandler handler = null;


        handler = new BridgeHandler() {
            @Override
            public void handler(String data, CallBackHandler<String> function) {
                try {
                    if(core.getMethodMap()!=null){
                        core.getMethodMap().invokeWithFields(name,data,function);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        return handler;

    }


    private static void register(String namespace,final Object obj,final Method method,
                                 IBridgeCore core,boolean isInterceptor,boolean registerHandler){
        core.register(namespace,method.getName(),method,obj);
        if(registerHandler){
            core.registerHandler(method.getName(), getProxyHandler(method.getName(), core),isInterceptor);
        }
    }


    public static boolean iterSendInterceptor(IBridgeCore core,Object[] args){
        boolean isInterceptor = false;
        if(core != null){
            List<BridgeInterceptor> interceptors = core.getInterceptor();
            for(int i = 0;i<interceptors.size();i++){
                if(args ==null){
                    isInterceptor |= false;
                }else if(args.length == 3){
                    isInterceptor |= interceptors.get(i).sendInterceptor((String)args[0],(String)args[1]);
                }else if(args.length == 2){
                    if(args[1] instanceof CallBackHandler){
                        isInterceptor |= interceptors.get(i).sendInterceptor((String)args[0],null);
                    }else {
                        isInterceptor |= interceptors.get(i).sendInterceptor((String)args[0],args[1].toString());
                    }
                }else if(args.length == 1){
                    isInterceptor |= interceptors.get(i).sendInterceptor((String)args[0],null);
                }else if(args.length == 0){
                    isInterceptor |= interceptors.get(i).sendInterceptor(null,null);
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
                if(args ==null){
                    isInterceptor |= false;
                }if(args.length == 2){
                    isInterceptor |= interceptors.get(i).receiverInterceptor(name, args[0]);
                }else if(args.length == 1){
                    isInterceptor |= interceptors.get(i).receiverInterceptor(name, args[0]);
                }else if(args.length == 0){
                    isInterceptor |= interceptors.get(i).receiverInterceptor(name, null);
                }

                if(isInterceptor){
                    return isInterceptor;
                }
            }
        }

        return isInterceptor;
    }



}
