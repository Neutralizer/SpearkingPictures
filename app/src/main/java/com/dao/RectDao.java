package com.dao;

import android.graphics.Point;
import android.graphics.Rect;

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

    @Query("Select * from rect_table where picture_name like :picName and internal_rect = :rect")
    SpeakingRect getRect(String picName, Rect rect);

    @Insert
    void insertRect(SpeakingRect rect);

    @Query("Update rect_table set picture_name = :picName, internal_rect = :rect where id = :id")
    void updateRect(int id, String picName, Rect rect);

    //TODO update all parameters constructor

    @Query("Delete from rect_table where picture_name like :picName")
    void deleteAllRectsOfAPic(String picName);

    @Query("Delete from rect_table where picture_name like :picName and internal_rect = :rect")
    void deleteRect(String picName, Rect rect);

    @Query("Delete from rect_table")
    void deleteAll();



}
