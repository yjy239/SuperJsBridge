package com.yjy.superbridge.internal;

import org.aspectj.lang.NoAspectBoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.List;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/14
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
@Aspect
public class BridgeAop {

    private static BridgeAop sInstance;

    @Around("execution(@com.yjy.superbridge.internal.ReceiverBridge * *(..)) && @annotation(bridge)")
    public Object receiverInterceptor(final ProceedingJoinPoint joinPoint, ReceiverBridge bridge){
        Object obj = null;

        String name = joinPoint.getSignature().getName();

        BridgeInterface bridgeInterface = (BridgeInterface)joinPoint.getThis();
        //全部为false 才能不拦截继续下去
        boolean isInterceptor = false;
        if(bridgeInterface != null){
            List<BridgeInterceptor> interceptors = bridgeInterface.getInterceptors();
            for(int i = 0;i<interceptors.size();i++){
                Object[] args = joinPoint.getArgs();
                if(interceptors.get(i) == null){
                    continue;
                }
                if(args.length == 2){
                    isInterceptor |= interceptors.get(i).receiverInterceptor(name, args);
                }else if(args.length == 1){
                    isInterceptor |= interceptors.get(i).receiverInterceptor(name, args);
                }else if(args.length == 0){
                    isInterceptor |= interceptors.get(i).receiverInterceptor(name, null);
                }

                if(isInterceptor){
                    return null;
                }
            }
        }


        try {
            obj = joinPoint.proceed();
        }catch (Throwable  e){
            e.printStackTrace();
        }

        return obj;

    }



    static Throwable mCause;

    public static BridgeAop aspectOf() {
        if (sInstance == null) {
            throw new NoAspectBoundException("com.yjy.superbridge.internal.BridgeAop", mCause);
        } else {
            return sInstance;
        }
    }

    public static boolean hasAspect() {
        return sInstance != null;
    }

    static {
        try {
            sInstance = new BridgeAop();
        } catch (Throwable var1) {
            mCause = var1;
        }

    }

}
