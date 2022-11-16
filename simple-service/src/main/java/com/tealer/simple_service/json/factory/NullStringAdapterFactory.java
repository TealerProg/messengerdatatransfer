package com.tealer.simple_service.json.factory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.tealer.simple_service.json.adapter.NullStringAdapter;


public class NullStringAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (!String.class.isAssignableFrom(rawType)) {
            return null;
        }
        return (TypeAdapter<T>) new NullStringAdapter(gson);
    }

}