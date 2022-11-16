package com.tealer.simple_service.json.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tealer.simple_service.json.NumberUtils;

public class NullFloatTypeAdapter extends TypeAdapter<Float> {
    @Override
    public void write(JsonWriter out, Float value) {
        try {
            if (value == null){
                value = 0F;
            }
            out.value(value.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Float read(JsonReader in) {
        try {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return 0F;
            }
            if (in.peek() == JsonToken.BOOLEAN) {
                return in.nextBoolean() ? 1F : 0F;//如果为true则返回为int的1，false返回0.
            }
            if (in.peek() == JsonToken.STRING) {
                String str = in.nextString();
                if (NumberUtils.isNumeric(str)){
                    return NumberUtils.convertToFloat(str,0F);
                } else {
                    return 0F;
                }
            } else {
                return NumberUtils.convertToFloat(in.nextString(),0F);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0F;
    }
}
