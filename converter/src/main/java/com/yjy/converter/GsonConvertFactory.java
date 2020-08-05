package com.yjy.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.yjy.superbridge.internal.convert.ConvertFactory;
import com.yjy.superbridge.internal.convert.Converter;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GsonConvertFactory implements ConvertFactory {
    private Gson mGson;
    public static GsonConvertFactory create(Gson gson){
        return new GsonConvertFactory(gson);
    }

    private GsonConvertFactory(Gson gson){
        mGson = gson;
    }

    @Override
    public Converter createConverter(Type type) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new GsonConverter<>(mGson,adapter);
    }
}
