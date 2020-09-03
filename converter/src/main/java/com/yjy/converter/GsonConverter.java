package com.yjy.converter;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.yjy.superbridge.internal.convert.ChildConvertFactory;
import com.yjy.superbridge.internal.convert.Converter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

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
    private ChildConvertFactory factory;
    public GsonConverter(Gson gson, TypeAdapter<T> adapter, ChildConvertFactory factory) {
        mGson = gson;
        this.mAdapter = adapter;
        this.factory = factory;

    }


    @Override
    public T convert(String value) throws IOException {
        JsonReader jsonReader = mGson.newJsonReader(new StringReader(value));
        return (T)mAdapter.read(jsonReader);
    }

    @Override
    public String toConvert(T value) throws IOException {
        if (value == null) {
            return mGson.toJson(JsonNull.INSTANCE);
        }

        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = newJsonWriter(Streams.writerForAppendable(writer),factory);
        mGson.toJson(value, value.getClass(), jsonWriter);
        return mGson.toJson(value);
    }

    private JsonWriter newJsonWriter(Writer writer,ChildConvertFactory factory) throws IOException {
        WalkerJsonWriter jsonWriter = new WalkerJsonWriter(writer,factory);
        jsonWriter.setSerializeNulls(false);
        return jsonWriter;
    }
}
