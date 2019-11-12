package com.yjy.internal;

import android.net.http.SslError;
import android.os.Message;
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

import com.yjy.superjsbridgedemo.internal.IWebViewClientInterceptor;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/11/08
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public abstract class BaseWebViewClientInterceptpr implements IWebViewClientInterceptor {


    @Override
    public boolean onLoadResource(WebView view, String url) {
        return false;
    }

    @Override
    public boolean onPageCommitVisible(WebView view, String url) {
        return false;
    }



    @Override
    public boolean onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        return false;
    }

    @Override
    public boolean onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        return false;
    }

    @Override
    public boolean onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        return false;
    }

    @Override
    public boolean onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        return false;
    }

    @Override
    public boolean onFormResubmission(WebView view, Message dontResend, Message resend) {
        return false;
    }

    @Override
    public boolean doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        return false;
    }

    @Override
    public boolean onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        return false;
    }

    @Override
    public boolean onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        return false;
    }

    @Override
    public boolean onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        return false;
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onUnhandledKeyEvent(WebView view, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onScaleChanged(WebView view, float oldScale, float newScale) {
        return false;
    }

    @Override
    public boolean onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args) {
        return false;
    }

    @Override
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        return false;
    }

    @Override
    public boolean onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) {
        return false;
    }
}
