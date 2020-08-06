package com.yjy.superjsbridgedemo;

import android.util.Log;

import com.yjy.superbridge.internal.BridgeField;
import com.yjy.superbridge.internal.BridgeInterface;
import com.yjy.superbridge.internal.BridgeMethod;
import com.yjy.superbridge.internal.CallBackHandler;
import com.yjy.superbridge.jsbridge.CallBackFunction;
import com.yjy.superjsbridgedemo.model.User;

import javax.security.auth.callback.CallbackHandler;

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

    @BridgeMethod(interceptor = true)
    public void test1(String s){
        Log.e("JSTest",s) ;
    }

    @BridgeMethod(interceptor = true)
    public void submitFromWeb(@BridgeField(name = "param") String data, CallBackFunction function){
        function.complete("submitFromWeb response: "+data);
    }

    @BridgeMethod(interceptor = true)
    public void submitUserFromWeb(@BridgeField(name = "user") User data, CallBackFunction function){
        function.complete("submitFromWeb response: "+data.getUsername());
    }


//    @ReceiverBridge
//    public void submitFromWeb(String s2, CallBackFunction function){
//       function.onCallBack("submitFromWeb response: "+s2);
//    }



}
