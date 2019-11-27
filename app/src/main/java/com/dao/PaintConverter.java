package com.dao;

import android.graphics.Paint;
import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;


public class PaintConverter {

    @TypeConverter
    public static Paint stringToPaint(String value) {
        if(value == null){
            return new Paint();
        }
//        Paint paint = new Gson().fromJson(value,Paint.class);
//        return paint;
        List<String> list = Arrays.asList(value.split(","));
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Integer.parseInt(list.get(0)));
        paint.setStrokeWidth(Float.parseFloat(list.get(1)));
        paint.setAlpha(Integer.parseInt(list.get(2)));
        return paint;
    }

    @TypeConverter
    public static String paintToString(Paint value) {
        if(value == null){
            return null;
        }
//        String json = new Gson().toJson(value);
//        return json;
        String result = value.getColor() + "," +  value.getStrokeWidth() + "," + value.getAlpha();
        return result;
    }
}
