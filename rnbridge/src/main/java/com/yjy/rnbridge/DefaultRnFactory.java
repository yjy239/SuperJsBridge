package com.yjy.rnbridge;

import com.facebook.react.ReactInstanceManager;
import com.yjy.superbridge.internal.IBridgeClient;
import com.yjy.superbridge.internal.IBridgeCore;
import com.yjy.superbridge.internal.IBridgeFactory;
import com.yjy.superbridge.internal.ReceiveFromPlatformCallback;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DefaultRnFactory implements IBridgeFactory<RnReceiveFromPlatformCallback> {
    private ReactInstanceManager mReactInstanceManager;
    public DefaultRnFactory(ReactInstanceManager manager){
        mReactInstanceManager = manager;
    }
    @Override
    public IBridgeCore getBridgeCore() {
        return new RnCore(mReactInstanceManager);
    }

    @Override
    public IBridgeClient getBridgeClient() {
        return null;
    }

    @Override
    public IBridgeFactory setReceiveFromPlatformCallback(RnReceiveFromPlatformCallback callback) {
        return null;
    }

    @Override
    public RnReceiveFromPlatformCallback getReceiveFromPlatformCallback() {
        return null;
    }
}
