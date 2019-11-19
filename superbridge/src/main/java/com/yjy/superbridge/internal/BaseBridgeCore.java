package com.yjy.superbridge.internal;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/15
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public abstract class BaseBridgeCore implements IBridgeCore{

    private List<BridgeInterceptor> mInterceptors = new ArrayList<>();

    @Override
    public void addInterceptor(BridgeInterceptor interceptor) {
        mInterceptors.add(interceptor);
    }

    @Override
    public void removeInterceptor(BridgeInterceptor interceptor) {
        mInterceptors.remove(interceptor);
    }

    @Override
    public void setInterceptor(List<BridgeInterceptor> interceptors) {
        mInterceptors.addAll(interceptors);
    }

    @Override
    public List<BridgeInterceptor> getInterceptor() {
        return mInterceptors;
    }
}
