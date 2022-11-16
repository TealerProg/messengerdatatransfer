package com.tealer.simple_service.json.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tealer.simple_service.json.NumberUtils;


public class NullDoubleTypeAdapter extends TypeAdapter<Double> {
    @Override
    public void write(JsonWriter out, Double value) {
        try {
            if (value == null){
                value = 0D;
            }
            out.value(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Double read(JsonReader in) {
        try {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return 0D;
            }
            if (in.peek() == JsonToken.BOOLEAN) {
                //如果为true则返回为int的1，false返回0.
                return in.nextBoolean() ? 1D : 0D;
            }
            if (in.peek() == JsonToken.STRING) {
                String str = in.nextString();
                if (NumberUtils.isNumeric(str)){
                    return NumberUtils.convertToDouble(str,0D);
                } else {
                    return 0D;
                }
            } else {
                Double value = in.nextDouble();
                return value == null ? 0D : value;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 0D;
    }
}
