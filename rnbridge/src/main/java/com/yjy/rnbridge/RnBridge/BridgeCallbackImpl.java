package com.yjy.rnbridge.RnBridge;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.JSInstance;
import com.yjy.superbridge.internal.CallBackHandler;

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

    public BridgeCallbackImpl(JSInstance jsInstance, int callbackId) {
        mJSInstance = jsInstance;
        mCallbackId = callbackId;
        mInvoked = false;
    }

    @Override
    public void complete(Object data) {
        if (mInvoked) {
            throw new RuntimeException(
                    "Illegal callback invocation from native "
                            + "module. This callback type only permits a single invocation from "
                            + "native code.");
        }
        mJSInstance.invokeCallback(mCallbackId, Arguments.fromJavaArgs(new Object[]{data}));
        mInvoked = true;
    }

    @Override
    public void complete() {
        complete(null);
    }

    @Override
    public void setProgressData(Object value) {

    }
}
