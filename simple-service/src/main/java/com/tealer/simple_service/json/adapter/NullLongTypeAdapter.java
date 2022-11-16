package com.tealer.simple_service.json.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tealer.simple_service.json.NumberUtils;

public class NullLongTypeAdapter extends TypeAdapter<Long> {
    @Override
    public void write(JsonWriter out, Long value) {
        try {
            if (value == null){
                value = 0L;
            }
            out.value(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long read(JsonReader in){
        try {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return 0L;
            }
            if (in.peek() == JsonToken.BOOLEAN) {
                return in.nextBoolean() ? 1L : 0L;//如果为true则返回为int的1，false返回0.
            }
            if (in.peek() == JsonToken.STRING) {
                String str = in.nextString();
                if (NumberUtils.isNumeric(str)){
                    return NumberUtils.convertToLong(str,0L);
                } else {
                    return 0L;
                }
            } else {
                return in.nextLong();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
