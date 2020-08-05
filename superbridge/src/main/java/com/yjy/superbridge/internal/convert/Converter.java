package com.yjy.superbridge.internal.convert;

import java.io.IOException;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface Converter<F,T> {
   T convert(F value) throws IOException;
}
