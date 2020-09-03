package com.yjy.rnbridge.RnBridge;

import android.text.TextUtils;
import android.util.Log;

import com.facebook.react.bridge.JSInstance;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.yjy.superbridge.internal.CallBackHandler;
import com.yjy.superbridge.internal.convert.ChildConvertFactory;
import com.yjy.superbridge.internal.convert.ConvertFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BridgeCallbackImpl implements CallBackHandler {
    private final JSInstance mJSInstance;
    private final int mCallbackId;
    private boolean mInvoked;
    private ConvertFactory mFactory;
    private Transformer transformer;

    private static final String TAG = BridgeCallbackImpl.class.getSimpleName();
    public BridgeCallbackImpl(JSInstance jsInstance, int callbackId, ConvertFactory factory) {
        mJSInstance = jsInstance;
        mCallbackId = callbackId;
        mInvoked = false;
        mFactory = factory;
        transformer = new Transformer();
    }

    @Override
    public void complete(Object data) {
        if (mInvoked) {
            throw new RuntimeException(
                    "Illegal callback invocation from native "
                            + "module. This callback type only permits a single invocation from "
                            + "native code.");
        }
        mJSInstance.invokeCallback(mCallbackId, fromJavaArgs(new Object[]{data}));
        mInvoked = true;
    }

    public WritableNativeArray fromJavaArgs(Object[] args) {
        WritableNativeArray arguments = new WritableNativeArray();
        for (int i = 0; i < args.length; i++) {
            Object argument = args[i];
            if (argument == null) {
                arguments.pushNull();
                continue;
            }

            Class argumentClass = argument.getClass();
            if (argumentClass == Boolean.class) {
                arguments.pushBoolean(((Boolean) argument).booleanValue());
            } else if (argumentClass == Integer.class) {
                arguments.pushDouble(((Integer) argument).doubleValue());
            } else if (argumentClass == Double.class) {
                arguments.pushDouble(((Double) argument).doubleValue());
            } else if (argumentClass == Float.class) {
                arguments.pushDouble(((Float) argument).doubleValue());
            } else if (argumentClass == String.class) {
                arguments.pushString(argument.toString());
            } else if (argumentClass == WritableNativeMap.class) {
                arguments.pushMap((WritableNativeMap) argument);
            } else if (argumentClass == WritableNativeArray.class) {
                arguments.pushArray((WritableNativeArray) argument);
            }else if(mFactory!=null&&
                    (Collection.class.isAssignableFrom(argumentClass)||argumentClass.isArray())){
                try {
                    mFactory.createConverter(argument.getClass(),transformer).toConvert(argument);
                    WritableNativeArray array = (WritableNativeArray)transformer.getResult();
                    Log.e("result",array!=null?array.toString():null);
                    arguments.pushArray(array);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else if(mFactory!=null){
                try {
                    mFactory.createConverter(argument.getClass(),transformer).toConvert(argument);
                    WritableNativeMap map = (WritableNativeMap)transformer.getResult();
                    Log.e("result",map!=null?map.toString():null);
                    arguments.pushMap(map);
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                throw new RuntimeException("Cannot convert argument of type " + argumentClass);
            }
        }
        return arguments;
    }

    @Override
    public void complete() {
        complete(null);
    }

    @Override
    public void setProgressData(Object value) {

    }

}
