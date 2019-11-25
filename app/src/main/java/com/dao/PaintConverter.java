package com.dao;

import android.graphics.Paint;
import androidx.room.TypeConverter;

import com.google.gson.Gson;


public class PaintConverter {

    @TypeConverter
    public static Paint stringToPaint(String value) {
        if(value == null){
            return new Paint();
        }
        Paint paint = new Gson().fromJson(value,Paint.class);
        return paint;
    }

    @TypeConverter
    public static String paintToString(Paint value) {
        if(value == null){
            return null;
        }
        String json = new Gson().toJson(value);
        return json;
    }
}
