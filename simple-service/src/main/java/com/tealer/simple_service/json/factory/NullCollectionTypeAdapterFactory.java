package com.tealer.simple_service.json.factory;

import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.tealer.simple_service.json.adapter.NullCollectionTypeAdapter;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;

public class NullCollectionTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;

    public NullCollectionTypeAdapterFactory() {
        constructorConstructor = new ConstructorConstructor(new HashMap<Type, InstanceCreator<?>>());
    }

    public NullCollectionTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();

        Class<? super T> rawType = typeToken.getRawType();
        if (!Collection.class.isAssignableFrom(rawType)) {
            return null;
        }

        Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
        TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
        ObjectConstructor<T> constructor = constructorConstructor.get(typeToken);

        TypeAdapter<T> result = new NullCollectionTypeAdapter(gson, elementType, elementTypeAdapter, constructor);
        return result;
    }
}
