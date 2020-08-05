package com.yjy.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.yjy.superbridge.internal.convert.Converter;

import java.io.IOException;
import java.io.StringReader;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GsonConverter<T> implements Converter<String,T> {
    private Gson mGson;
    private TypeAdapter mAdapter;
    public GsonConverter(Gson gson, TypeAdapter<T> adapter) {
        mGson = gson;
        this.mAdapter = adapter;
    }


    @Override
    public T convert(String value) throws IOException {
        JsonReader jsonReader = mGson.newJsonReader(new StringReader(value));
        return (T)mAdapter.read(jsonReader);
    }
}
