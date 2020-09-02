package com.yjy.rnbridge.RnCompent.dynamic;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.CatalystInstance;
import com.yjy.superbridge.internal.MethodMap;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/09/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BridgeJsModule {
    final CatalystInstance mCatalystInstance;
    public BridgeJsModule(CatalystInstance catalystInstance){
        this.mCatalystInstance = catalystInstance;
    }

    /**
     *
     * @param namespace moduleName.methodName
     * @param args method args
     */
    public void calHandler(String namespace,String args){
        calHandler(namespace,new Object[]{args});
    }

    /**
     *
     * @param namespace moduleName.methodName
     * @param args method args
     */
    public void calHandler(String namespace,Object[] args){
        String[] names = MethodMap.parseNamespace(namespace);
        String moduleName = names[0];
        String methodName = names[1];
        calHandler(moduleName,methodName,args);
    }

    public void calHandler(String moduleName,String methodName,Object[] args){
        mCatalystInstance.callFunction(moduleName,methodName,Arguments.fromJavaArgs(args));
    }
}
