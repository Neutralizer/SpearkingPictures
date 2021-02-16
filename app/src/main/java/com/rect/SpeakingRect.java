package com.rect;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "rect_table")
public class SpeakingRect {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "picture_name")
    private String picName;
    @ColumnInfo(name = "rect_paint")
    private Paint paint;//TODO move paint in separate class ?
    @ColumnInfo(name = "internal_rect")
    private Rect rect;

    public SpeakingRect(int id, String picName, Rect rect, Paint paint) {
        this.id = id;
        this.picName = picName;
        this.paint = paint;
        this.rect = rect;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public Rect getRect() {
        return rect;
    }

    public Paint getPaint(){
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    /**
     * 1-10
     * @param thickness rectangle border thickness
     */
    public void setBorderThickness(int thickness){
        this.paint.setStrokeWidth(thickness);
    }

    public int getBorderThickness(){
        return (int) this.paint.getStrokeWidth();
    }

    /**
     * Color.RED (example)
     * @param color the color number
     */
    public void setColor(int color){
        paint.setColor(color);
    }

    public int getColor(){
        return paint.getColor();
    }


    /**
     * 0-255
     * @param alpha transparency
     */
    public void setAlpha(int alpha) {
        this.paint.setAlpha(alpha);
    }

    public int getAlpha() {
        return this.paint.getAlpha();
    }

    /**
     * Is the new rect touching this one
     * @param newPoint //TODO redesign - will it use point or compare the 4 points of the 2 rects
     * @param newSize - if upper is true this is not needed
     * @return true if the 2 rectangles are touching (if not using point)
     */
    public boolean areRectanglesIntersecting(Point newPoint, int newSize){//TODO check for all 4 points
        if(//TODO check for out of bounds
           this.rect.contains(newPoint.x - newSize, newPoint.y - newSize) &&
           this.rect.contains(newPoint.x + newSize,newPoint.y + newSize)
        ){
            return true;
        }
        return false;

    }

}
