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

//    @Ignore
//    public SpeakingRect(int id, String picName, Point point, int borderThickness, int size, int color, int alpha) {
//        this.id = id;
//        this.picName = picName;
//        this.paint = new Paint();
//        this.rect = new Rect();
//        this.point = point;
//        this.borderThickness = borderThickness;
//        this.size = size;
//        this.color = color;
//        this.alpha = alpha;
//        setRectPosition(this.point);
//        paint.setColor(this.color);
//        paint.setStrokeWidth(this.borderThickness);
//        paint.setAlpha(this.alpha);//1-255
//        paint.setStyle(Paint.Style.STROKE);
//    }

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
     * @param thickness
     */
    public void setBorderThickness(int thickness){
        this.paint.setStrokeWidth(thickness);
    }

    public int getBorderThickness(){
        return (int) this.paint.getStrokeWidth();
    }

    /**
     * Color.RED (example)
     * @param color
     */
    public void setColor(int color){
        paint.setColor(color);
    }

    public int getColor(){
        return paint.getColor();
    }


    /**
     * 0-255
     * @param alpha
     */
    public void setAlpha(int alpha) {
        this.paint.setAlpha(alpha);
    }

    public int getAlpha() {
        return this.paint.getAlpha();
    }

    /**
     * Is the new rect touching this one
     * @param newPoint
     * @param newSize
     * @return
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
