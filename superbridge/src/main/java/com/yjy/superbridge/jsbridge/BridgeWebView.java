package com.yjy.superbridge.jsbridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.yjy.superbridge.internal.IWebView;


@SuppressLint("SetJavaScriptEnabled")
public class BridgeWebView extends WebView implements IWebView {

	private  JsBridgeCore mCore;


	public BridgeWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}

	public BridgeWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public BridgeWebView(Context context) {
		super(context);
		init();
	}


    private void init() {
		this.setVerticalScrollBarEnabled(false);
		this.setHorizontalScrollBarEnabled(false);
		this.getSettings().setJavaScriptEnabled(true);
	}









}
