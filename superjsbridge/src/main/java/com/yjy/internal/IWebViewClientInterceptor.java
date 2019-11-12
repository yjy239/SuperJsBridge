package com.yjy.internal;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/08
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public interface IWebViewClientInterceptor {
    boolean shouldOverrideUrlLoading(WebView view, String url);


    boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request);


    boolean onPageStarted(WebView view, String url, Bitmap favicon);


    boolean onPageFinished(WebView view, String url);

    boolean onLoadResource(WebView view, String url);


    boolean onPageCommitVisible(WebView view, String url);


    boolean shouldInterceptRequest(WebView view, String url);



    boolean shouldInterceptRequest(WebView view, WebResourceRequest request) ;


    boolean onTooManyRedirects(WebView view, android.os.Message cancelMsg, android.os.Message continueMsg);


    boolean onReceivedError(WebView view, int errorCode, String description, String failingUrl);


    boolean onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) ;


    boolean onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse);

    boolean onFormResubmission(WebView view, android.os.Message dontResend, android.os.Message resend);

    boolean doUpdateVisitedHistory(WebView view, String url, boolean isReload) ;


    boolean onReceivedSslError(WebView view, SslErrorHandler handler, SslError error);


    boolean onReceivedClientCertRequest(WebView view, ClientCertRequest request);


    boolean onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm);


    boolean shouldOverrideKeyEvent(WebView view, KeyEvent event);


    boolean onUnhandledKeyEvent(WebView view, KeyEvent event);


    boolean onScaleChanged(WebView view, float oldScale, float newScale);


    boolean onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args);


    boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) ;


    boolean onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) ;
}
