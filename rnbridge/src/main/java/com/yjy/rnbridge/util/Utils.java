package com.yjy.rnbridge.util;

import com.facebook.react.module.model.ReactModuleInfo;
import com.yjy.rnbridge.BridgeModuleHolder;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Utils {

    public static ReactModuleInfo getInfo(String name,Object object){
        return  new ReactModuleInfo(name,
                object.getClass().getSimpleName(),
                false,true,
                true,
                false,false);
    }


}
