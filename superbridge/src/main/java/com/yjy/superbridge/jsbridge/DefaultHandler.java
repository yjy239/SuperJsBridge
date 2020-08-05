package com.yjy.superbridge.jsbridge;

import com.yjy.superbridge.internal.CallBackHandler;

public class DefaultHandler implements BridgeHandler{

	String TAG = "DefaultHandler";
	
	@Override
	public void handler(String data, CallBackHandler<String> function) {
		if(function != null){
			function.complete("DefaultHandler response data");
		}
	}

}
