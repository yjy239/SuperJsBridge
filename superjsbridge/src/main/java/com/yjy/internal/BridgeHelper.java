package com.yjy.internal;

import com.yjy.superjsbridgedemo.internal.IRegister;
import com.yjy.superjsbridgedemo.internal.IWebView;
import com.yjy.superjsbridgedemo.internal.IWebViewClientInterceptor;
import com.yjy.superjsbridgedemo.jsbridge.BridgeHandler;
import com.yjy.superjsbridgedemo.jsbridge.BridgeWebViewClient;
import com.yjy.superjsbridgedemo.jsbridge.JsBridgeCore;

import java.util.Map;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/08
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class BridgeHelper {
    /**
     * 初始化，批量注入
     * @param webView
     * @param register
     * @param interceptor
     */
    public static void init(IWebView webView, IRegister register, IWebViewClientInterceptor interceptor){
        JsBridgeCore core = new JsBridgeCore(webView);

        if(register != null&&register.getRegisterMap()!=null){
            Map<String, BridgeHandler> map = register.getRegisterMap();
            for(Map.Entry<String, BridgeHandler> entry : map.entrySet()){
                core.registerHandler(entry.getKey(),entry.getValue());
            }
        }

        webView.setWebViewClient(new BridgeWebViewClient(core,interceptor));
    }
}
