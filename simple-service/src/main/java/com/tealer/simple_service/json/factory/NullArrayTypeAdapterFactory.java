package com.tealer.simple_service.json.factory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.tealer.simple_service.json.adapter.NullArrayTypeAdapter;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;


public class NullArrayTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        if (!(type instanceof GenericArrayType || type instanceof Class && ((Class<?>) type).isArray())) {
            return null;
        }

        Type componentType = $Gson$Types.getArrayComponentType(type);
        TypeAdapter<?> componentTypeAdapter = gson.getAdapter(TypeToken.get(componentType));
        return new NullArrayTypeAdapter(gson, componentTypeAdapter, $Gson$Types.getRawType(componentType));
    }


}
