package com.tealer.simple_service.json.factory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.tealer.simple_service.json.adapter.NullMultiDateAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class NullMultiDateAdapterFactory implements TypeAdapterFactory {
    private static final String[] DEFAULT_FORMATS = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd"};
    private final List<DateFormat> dateFormats;

    public NullMultiDateAdapterFactory() {
        this(DEFAULT_FORMATS);
    }

    public NullMultiDateAdapterFactory(String... stringDateFormats) {
        int size = stringDateFormats.length;
        dateFormats = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            dateFormats.add(new SimpleDateFormat(stringDateFormats[i], Locale.getDefault()));
        }
    }

    public NullMultiDateAdapterFactory(List<DateFormat> dateFormats) {
        this.dateFormats = new ArrayList<>(dateFormats);
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (!Date.class.isAssignableFrom(rawType)) {
            return null;
        }
        return (TypeAdapter<T>) new NullMultiDateAdapter(dateFormats);
    }
}
