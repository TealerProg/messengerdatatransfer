package com.tealer.simple_service.json.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;


public  class NullCollectionTypeAdapter<E> extends TypeAdapter<Collection<E>> {
    private final TypeAdapter<E> elementTypeAdapter;
    private final ObjectConstructor<? extends Collection<E>> constructor;

    public NullCollectionTypeAdapter(Gson context, Type elementType,
                   TypeAdapter<E> elementTypeAdapter,
                   ObjectConstructor<? extends Collection<E>> constructor) {
        this.elementTypeAdapter =
                new RuntimeAdapterTypeWrapper<E>(context, elementTypeAdapter, elementType);
        this.constructor = constructor;
    }

    @Override
    public Collection<E> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return constructor.construct();
        }

        Collection<E> collection = constructor.construct();
        in.beginArray();
        while (in.hasNext()) {
            E instance = elementTypeAdapter.read(in);
            collection.add(instance);
        }
        in.endArray();
        return collection;
    }

    @Override
    public void write(JsonWriter out, Collection<E> collection) throws IOException {
        if (collection == null) {
            out.nullValue();
            return;
        }
        out.beginArray();
        for (E element : collection) {
            elementTypeAdapter.write(out, element);
        }
        out.endArray();
    }
}