package com.yjy.superbridge.internal;

import com.yjy.superbridge.jsbridge.BridgeHandler;
import com.yjy.superbridge.jsbridge.CallBackFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/14
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class BridgeInterface {
    private List<BridgeInterceptor> mInterceptors = new ArrayList<>();

    public List<BridgeInterceptor> getInterceptors() {
        return mInterceptors;
    }

    public void setInterceptors(List<BridgeInterceptor> mInterceptors) {
        this.mInterceptors = mInterceptors;
    }
}
