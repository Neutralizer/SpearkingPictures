package com.dao;

import android.graphics.Point;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.rect.SpeakingRect;

import java.util.List;

@Dao
public interface RectDao {

    @Query("Select * from rect_table")
    List<SpeakingRect> getAllRects();

    @Query("Select * from rect_table where picture_name like :picName")
    List<SpeakingRect> getAllRectsOfAPic(String picName);

    @Query("Select * from rect_table where picture_name like :picName and point_location like :point")
    SpeakingRect getRect(String picName, Point point);

    @Insert
    void insertRect(SpeakingRect rect);

    @Query("Update rect_table set picture_name = :picName, point_location = :point where id = :id")
    void updateRect(int id, String picName, Point point);

    //TODO update all parameters constructor

    @Query("Delete from rect_table where picture_name like :picName")
    void deleteAllRectsOfAPic(String picName);

    @Query("Delete from rect_table where picture_name like :picName and point_location like :point")
    void deleteRect(String picName, Point point);

    @Query("Delete from rect_table")
    void deleteAll();



}
