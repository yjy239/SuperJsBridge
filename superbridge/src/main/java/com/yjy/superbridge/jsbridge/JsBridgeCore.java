package com.yjy.superbridge.jsbridge;

import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;

import com.yjy.superbridge.internal.BaseBridgeCore;
import com.yjy.superbridge.internal.BridgeHelper;
import com.yjy.superbridge.internal.IWebView;
import com.yjy.superbridge.internal.ProxyHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class JsBridgeCore extends BaseBridgeCore implements WebViewJavascriptBridge{
    private final String TAG = "JsBridgeCore";

    public static final String toLoadJs = "WebViewJavascriptBridge.js";
    Map<String, CallBackFunction> responseCallbacks = new HashMap<String, CallBackFunction>();
    Map<String, BridgeHandler> messageHandlers = new HashMap<String, BridgeHandler>();
    BridgeHandler defaultHandler = new DefaultHandler();


    private List<Message> startupMessage = new ArrayList<Message>();



    //刷新js中的queue
    private final static int FETCH_QUEUE_STEP = 1;

    private IWebView mWebView;

    public JsBridgeCore(IWebView webView){
        if(webView == null){
            throw new IllegalArgumentException("webview must not be null");
        }
        this.mWebView = webView;
    }


    public List<Message> getStartupMessage() {
        return startupMessage;
    }

    public void setStartupMessage(List<Message> startupMessage) {
        this.startupMessage = startupMessage;
    }

    private long uniqueId = 0;

    public void setDefaultHandler(BridgeHandler handler) {
        this.defaultHandler = handler;
    }

    public BridgeHandler getDefaultHandler(){
        return this.defaultHandler;
    }




    /**
     * 获取到CallBackFunction data执行调用并且从数据集移除
     * @param url
     */
    void handlerReturnData(String url) {
        String functionName = BridgeUtil.getFunctionFromReturnUrl(url);
        CallBackFunction f = responseCallbacks.get(functionName);
        String data = BridgeUtil.getDataFromReturnUrl(url);
        if (f != null) {
            f.complete(data);
            responseCallbacks.remove(functionName);
            return;
        }
    }

    @Override
    public void send(String data) {
        send(data, null);
    }


    @Override
    public void send(String data, CallBackFunction responseCallback) {
        doSend(null, data, responseCallback);
    }

    /**
     * 保存message到消息队列
     * @param handlerName handlerName
     * @param data data
     * @param responseCallback CallBackFunction
     */
    private void doSend(String handlerName, String data, CallBackFunction responseCallback) {
        Message m = new Message();
        if (!TextUtils.isEmpty(data)) {
            m.setData(data);
        }
        if (responseCallback != null) {
            String callbackStr = String.format(BridgeUtil.CALLBACK_ID_FORMAT, ++uniqueId + (BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()));
            responseCallbacks.put(callbackStr, responseCallback);
            m.setCallbackId(callbackStr);
        }
        if (!TextUtils.isEmpty(handlerName)) {
            m.setHandlerName(handlerName);
        }
        queueMessage(m);
    }

    /**
     * list<message> != null 添加到消息集合否则分发消息
     * @param m Message
     */
    void queueMessage(Message m) {
        if (startupMessage != null) {
            startupMessage.add(m);
        } else {
            dispatchMessage(m);
        }
    }

    /**
     * 分发message 必须在主线程才分发成功
     * @param m Message
     */
    void dispatchMessage(Message m) {
        String messageJson = m.toJson();
        //escape special characters for json string  为json字符串转义特殊字符
        messageJson = messageJson.replaceAll("(\\\\)([^utrn])", "\\\\\\\\$1$2");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(\")", "\\\\\"");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(\')", "\\\\\'");
        String javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, messageJson);
        // 必须要找主线程才会将数据传递出去 --- 划重点
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            mWebView.loadUrl(javascriptCommand);
        }
    }

    public void loadUrl(String jsUrl, CallBackFunction returnCallback) {
        mWebView.loadUrl(jsUrl);
        // 添加至 Map<String, CallBackFunction>
        responseCallbacks.put(BridgeUtil.parseFunctionName(jsUrl), returnCallback);
    }

    public void putResponseCallback(String jsUrl,CallBackFunction returnCallback){
        responseCallbacks.put(BridgeUtil.parseFunctionName(jsUrl), returnCallback);
    }

    public CallBackFunction getResponseCallback(String responseId){
        return responseCallbacks.get(responseId);
    }

    public void removeResponseCallback(String responseId){
        responseCallbacks.remove(responseId);
    }

    /**
     * register handler,so that javascript can call it
     * 注册处理程序,以便javascript调用它
     * @param handlerName handlerName
     * @param handler BridgeHandler
     */
    @Override
    public void registerHandler(String handlerName, BridgeHandler handler, boolean isInterceptor) {
        if(isInterceptor){
            handler = new ProxyHandler(this,handlerName,handler);
        }
        if (handler != null) {
            // 添加至 Map<String, BridgeHandler>
            messageHandlers.put(handlerName, handler);
        }
    }

    public BridgeHandler getHandler(String handlerName){
        return messageHandlers.get(handlerName);
    }

    /**
     * unregister handler
     *
     * @param handlerName
     */
    @Override
    public void unregisterHandler(String handlerName) {
        if (handlerName != null) {
            messageHandlers.remove(handlerName);
        }
    }

    /**
     * call javascript registered handler
     * 调用javascript处理程序注册
     * @param handlerName handlerName
     * @param data data
     * @param callBack CallBackFunction
     */
    @Override
    public void callHandler(String handlerName, String data, CallBackFunction callBack,boolean isInterceptor) {
        Object[] args = {handlerName,data,callBack};
        if(BridgeHelper.iterSendInterceptor(this,args)){
            return;
        }
        doSend(handlerName, data, callBack);
    }

    @Override
    public void registerObj(String name, Object obj) {
        BridgeHelper.registerInLow(name,obj,this,true);
    }


    @Override
    public void release() {
        mWebView = null;
    }


    /**
     * 刷新消息队列
     */
    void flushMessageQueue() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            loadUrl(BridgeUtil.JS_FETCH_QUEUE_FROM_JAVA, new CallBackFunction() {

                @Override
                public void complete(String data) {
                    // deserializeMessage 反序列化消息
                    List<Message> list = null;
                    try {
                        list = Message.toArrayList(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    if (list == null || list.size() == 0) {
                        return;
                    }

                    for (int i = 0; i < list.size(); i++) {
                        Message m = list.get(i);
                        String responseId = m.getResponseId();
                        // 是否是response  CallBackFunction
                        if (!TextUtils.isEmpty(responseId)) {
                            CallBackFunction function = responseCallbacks.get(responseId);
                            String responseData = m.getResponseData();
                            function.complete(responseData);
                            responseCallbacks.remove(responseId);
                        } else {
                            CallBackFunction responseFunction = null;
                            // if had callbackId 如果有回调Id
                            final String callbackId = m.getCallbackId();
                            if (!TextUtils.isEmpty(callbackId)) {
                                responseFunction = new CallBackFunction() {
                                    @Override
                                    public void complete(String data) {
                                        Message responseMsg = new Message();
                                        responseMsg.setResponseId(callbackId);
                                        responseMsg.setResponseData(data);
                                        queueMessage(responseMsg);
                                    }
                                };
                            } else {
                                responseFunction = new CallBackFunction() {
                                    @Override
                                    public void complete(String data) {
                                        // do nothing
                                    }
                                };
                            }
                            // BridgeHandler执行
                            BridgeHandler handler;
                            if (!TextUtils.isEmpty(m.getHandlerName())) {
                                handler = messageHandlers.get(m.getHandlerName());
                            } else {
                                handler = defaultHandler;
                            }
                            if (handler != null){
                                handler.handler(m.getData(), responseFunction);
                            }
                        }
                    }


                }

            });
        }
    }

}
