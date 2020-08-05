package com.yjy.superbridge.jsbridge;

import com.yjy.superbridge.internal.CallBackHandler;

public interface BridgeHandler {


	 void handler(String data, CallBackHandler<String> function);

}
