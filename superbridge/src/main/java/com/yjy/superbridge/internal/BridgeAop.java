package com.yjy.superbridge.internal;

import com.yjy.superbridge.jsbridge.CallBackFunction;

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

    @Around("execution(@com.yjy.superbridge.internal.ReceiverBridge * *(..)) && @annotation(bridge)")
    public Object receiverInterceptor(final ProceedingJoinPoint joinPoint, ReceiverBridge bridge){
        Object obj = null;

        BridgeInterface bridgeInterface = (BridgeInterface)joinPoint.getThis();
        boolean isContinue = true;
        if(bridgeInterface != null){
            List<BridgeInterceptor> interceptors = bridgeInterface.getInterceptors();
            for(int i = 0;i<interceptors.size();i++){
                Object[] args = joinPoint.getArgs();
                if(args.length == 2){
                    isContinue &= interceptors.get(i).receiverInterceptor(args[0],args[1]);
                }else if(args.length == 1){
                    isContinue &= interceptors.get(i).receiverInterceptor(args[0],null);
                }else if(args.length == 0){
                    isContinue &= interceptors.get(i).receiverInterceptor(null,null);
                }
            }
        }


        if(!isContinue){
            return null;
        }

        try {
            obj = joinPoint.proceed();
        }catch (Throwable  e){
            e.printStackTrace();
        }

        return obj;

    }



    @Around("execution(@com.yjy.superbridge.internal.SendBridge * *(..)) && @annotation(bridge)")
    public Object sendInterceptor(final ProceedingJoinPoint joinPoint, SendBridge bridge){
        Object obj = null;


        IBridgeCore core = (IBridgeCore)joinPoint.getTarget();
        boolean isContinue = true;
        if(core != null){
            List<BridgeInterceptor> interceptors = core.getInterceptor();
            for(int i = 0;i<interceptors.size();i++){
                Object[] args = joinPoint.getArgs();
                if(args.length == 3){
                    isContinue &= interceptors.get(i).sendInterceptor((String)args[0],(String)args[1],(CallBackFunction) args[2]);
                }else if(args.length == 2){
                    if(args[1] instanceof CallBackFunction){
                        isContinue &= interceptors.get(i).sendInterceptor((String)args[0],null,(CallBackFunction) args[1]);
                    }else {
                        isContinue &= interceptors.get(i).sendInterceptor((String)args[0],args[1].toString(),null );
                    }
                }else if(args.length == 1){
                    isContinue &= interceptors.get(i).sendInterceptor((String)args[0],null,null);
                }else if(args.length == 0){
                    isContinue &= interceptors.get(i).sendInterceptor(null,null,null);
                }
            }
        }


        if(!isContinue){
            return null;
        }


        try {
            obj = joinPoint.proceed();
        }catch (Throwable  e){
            e.printStackTrace();
        }

        return obj;

    }
}
