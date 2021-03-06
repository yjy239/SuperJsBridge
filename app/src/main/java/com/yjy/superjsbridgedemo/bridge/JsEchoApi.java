package com.yjy.superjsbridgedemo.bridge;

import android.util.Log;

import com.yjy.superbridge.internal.BridgeField;
import com.yjy.superbridge.internal.BridgeInterface;
import com.yjy.superbridge.internal.BridgeMethod;
import com.yjy.superbridge.internal.CallBackHandler;

import org.json.JSONException;



/**
 * Created by du on 16/12/31.
 */

public class JsEchoApi {

    @BridgeMethod(interceptor = true)
    public Object syn(@BridgeField(name = "msg") String msg,
                      @BridgeField(name = "tag") String tag) throws JSONException {
        Log.e("msg",msg);
        Log.e("tag",tag);
        return  msg;
    }

    @BridgeMethod
    public void asyn(@BridgeField(name = "msg") String msg,
                     @BridgeField(name = "tag") String tag, CallBackHandler handler){
        Log.e("msg",msg);
        Log.e("tag",tag);
        handler.complete(tag);
    }
}