package com.yjy.superjsbridgedemo.DSCompent;

import android.content.Context;
import android.util.AttributeSet;

import com.yjy.superbridge.internal.IWebView;
import com.yjy.superjsbridgedemo.DSBridge.DWebView;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/11
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class DSWebView extends DWebView implements IWebView {
    public DSWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DSWebView(Context context) {
        super(context);
    }
}
