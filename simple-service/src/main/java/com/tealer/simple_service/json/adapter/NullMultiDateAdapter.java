package com.tealer.simple_service.json.adapter;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class NullMultiDateAdapter extends TypeAdapter<Date> {
    private final List<DateFormat> dateFormats;

    public NullMultiDateAdapter(List<DateFormat> dateFormats) {
        this.dateFormats = dateFormats;
    }

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        synchronized (dateFormats) {
            if (value == null) {
                out.nullValue();
            } else {
                String dateString = dateFormats.get(0).format(value);
                out.value(dateString);
            }
        }
    }

    @Override
    public synchronized Date read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        if (token == JsonToken.NULL) {
            in.nextNull();
            return new Date();
        }
        if (token != JsonToken.STRING) {
            throw new JsonParseException("The date should be a string value");
        }
        String dateString = in.nextString();
        Date result = null;
        for (int i = 0, size = dateFormats.size(); result == null && i < size; ++i) {
            try {
                result = dateFormats.get(i).parse(dateString);
            } catch (ParseException ignore) {
            }
        }
        if (result == null) {
            throw new JsonParseException("Bad date format, the resource = " + dateString);
        }
        return result;
    }
}
