package com.yjy.superjsbridgedemo;

import android.util.Log;

import com.yjy.superbridge.internal.BridgeInterface;
import com.yjy.superbridge.internal.ReceiverBridge;
import com.yjy.superbridge.jsbridge.CallBackFunction;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/11
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class JsTest extends BridgeInterface {

    public void test1(String s){
        Log.e("JSTest",s) ;
    }



    @ReceiverBridge
    public void submitFromWeb(String s2, CallBackFunction function){
       function.onCallBack("submitFromWeb response: "+s2);
    }



}
