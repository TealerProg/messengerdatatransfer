package com.tealer.simple_service.json.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tealer.simple_service.json.NumberUtils;

public class NullIntegerTypeAdapter extends TypeAdapter<Integer> {
    @Override
    public void write(JsonWriter out, Integer value) {
        try {
            if (value == null){
                value = 0;
            }
            out.value(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer read(JsonReader in) {
        try {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return 0;
            }
            if (in.peek() == JsonToken.BOOLEAN) {
                return in.nextBoolean() ? 1 : 0;//如果为true则返回为int的1，false返回0.
            }
            if (in.peek() == JsonToken.STRING) {
                String str = in.nextString();
                if (NumberUtils.isNumeric(str)){
                    return NumberUtils.convertToInt(str,0);
                } else {
                    return 0;
                }
            } else {
                return in.nextInt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
