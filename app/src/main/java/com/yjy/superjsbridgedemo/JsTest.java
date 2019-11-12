package com.yjy.superjsbridgedemo;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.yjy.superbridge.jsbridge.BridgeHandler;
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
public class JsTest {

    public void test1(String s){
        Log.e("JSTest",s) ;
    }



    public void submitFromWeb(String s2, CallBackFunction function){
       function.onCallBack("response: "+s2);
    }



}
