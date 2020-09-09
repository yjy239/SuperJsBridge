package com.yjy.rnbridge.RnCompent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.WritableMap;
import com.yjy.superbridge.internal.CallBackHandler;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface PromiseCallback extends CallBackHandler {
    /**
     * Successfully resolve the Promise with an optional value.
     *
     * @param value Object
     */
    void resolve(@Nullable Object value);

    /**
     * Report an error without an exception using a custom code and error message.
     *
     * @param code String
     * @param message String
     */
    void reject(String code, String message);

    /**
     * Report an exception with a custom code.
     *
     * @param code String
     * @param throwable Throwable
     */
    void reject(String code, Throwable throwable);

    /**
     * Report an exception with a custom code and error message.
     *
     * @param code String
     * @param message String
     * @param throwable Throwable
     */
    void reject(String code, String message, Throwable throwable);

    /**
     * Report an exception, with default error code. Useful in catch-all scenarios where it's unclear
     * why the error occurred.
     *
     * @param throwable Throwable
     */
    void reject(Throwable throwable);

    /* ---------------------------
     *  With userInfo WritableMap
     * --------------------------- */

    /**
     * Report an exception, with default error code, with userInfo. Useful in catch-all scenarios
     * where it's unclear why the error occurred.
     *
     * @param throwable Throwable
     * @param userInfo WritableMap
     */
    void reject(Throwable throwable, WritableMap userInfo);

    /**
     * Reject with a code and userInfo WritableMap.
     *
     * @param code String
     * @param userInfo WritableMap
     */
    void reject(String code, @NonNull WritableMap userInfo);

    /**
     * Report an exception with a custom code and userInfo.
     *
     * @param code String
     * @param throwable Throwable
     * @param userInfo WritableMap
     */
    void reject(String code, Throwable throwable, WritableMap userInfo);

    /**
     * Report an error with a custom code, error message and userInfo, an error not caused by an
     * exception.
     *
     * @param code String
     * @param message String
     * @param userInfo WritableMap
     */
    void reject(String code, String message, @NonNull WritableMap userInfo);

    /**
     * Report an exception with a custom code, error message and userInfo.
     *
     * @param code String
     * @param message String
     * @param throwable Throwable
     * @param userInfo WritableMap
     */
    void reject(String code, String message, Throwable throwable, WritableMap userInfo);

    /* ------------
     *  Deprecated
     * ------------ */

    /**
     * Report an error which wasn't caused by an exception.
     *
     * @deprecated Prefer passing a module-specific error code to JS. Using this method will pass the
     *     error code "EUNSPECIFIED".
     */
    @Deprecated
    void reject(String message);
}
