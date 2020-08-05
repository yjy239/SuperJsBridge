package com.yjy.superbridge.jsbridge;

import com.yjy.superbridge.internal.CallBackHandler;

public abstract class CallBackFunction implements CallBackHandler<String> {
    @Override
    public void complete() {
        complete(null);
    }

    @Override
    public void setProgressData(String value) {
        complete(value);
    }
}
