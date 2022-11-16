package com.tealer.simple_service.json.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tealer.simple_service.json.NumberUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NullStringAdapter extends TypeAdapter<String> {
    private Gson gson = null;

    public NullStringAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public synchronized void write(JsonWriter out, String value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value);
    }

    @Override
    public synchronized String read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        switch (token) {
            case NULL:
                in.nextNull();
                return null;
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<Object>();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(readInside(in));
                }
                in.endArray();
                return gson.toJson(list);

            case BEGIN_OBJECT:
                Map<String, Object> map = new LinkedTreeMap<String, Object>();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), readInside(in));
                }
                in.endObject();
                return gson.toJson(map);

            case BOOLEAN:
                return gson.toJson(in.nextBoolean());

            default:
                return in.nextString();
        }
    }


    Object readInside(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        switch (token) {
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<Object>();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(readInside(in));
                }
                in.endArray();
                return list;

            case BEGIN_OBJECT:
                Map<String, Object> map = new LinkedTreeMap<String, Object>();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), readInside(in));
                }
                in.endObject();
                return map;

            case STRING:
                return in.nextString();

            case NUMBER:
                String value = in.nextString();
                if(NumberUtils.isInteger(value)){
                    return Long.valueOf(value);
                }else {
                    return Double.valueOf(value);
                }
            case BOOLEAN:
                return in.nextBoolean();

            case NULL:
                in.nextNull();
                return null;

            default:
                throw new IllegalStateException();
        }
    }
}