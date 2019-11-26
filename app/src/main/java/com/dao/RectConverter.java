package com.dao;


import android.graphics.Rect;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

public class RectConverter {
    @TypeConverter
    public static Rect stringToRect(String value) {
        if (value == null) {
            return new Rect();
        }
        Rect rect = new Gson().fromJson(value, Rect.class);
        return rect;
    }

    @TypeConverter
    public static String rectToString(Rect value) {
        if (value == null) {
            return null;
        }
        String json = new Gson().toJson(value);
        return json;
    }
}
