package com.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.rect.SpeakingRect;

@Database(entities = SpeakingRect.class, exportSchema = false, version = 3)
@TypeConverters({RectConverter.class, PaintConverter.class})
public abstract class RectDatabase extends RoomDatabase {

    private static final String DB_NAME = "rect_db";
    private static RectDatabase instance;

    public static synchronized RoomDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), RectDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract RectDao rectDao();
}
