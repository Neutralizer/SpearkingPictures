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
    @Ignore
    private Paint paint;//TODO move paint in separate class ?
    @Ignore
    private Rect rect;//TODO extend rect and drop the point? could be useful for touch expanding the rect
    @ColumnInfo(name = "point_location")
    private Point point;
    @ColumnInfo(name = "border_thickness")
    private int borderThickness;
    @ColumnInfo(name = "rect_size")
    private int size;
    @ColumnInfo(name = "border_color")
    private int color;
    @ColumnInfo(name = "border_alpha")
    private int alpha;

    public SpeakingRect(int id, String picName, Point point) {
        this.id = id;
        this.picName = picName;
        this.paint = new Paint();
        this.rect = new Rect();
        this.point = point;
        this.borderThickness = 3;
        this.size = 50;
        this.color = Color.RED;
        this.alpha = 100;
        setRectPosition(this.point);//TODO change to 4 sides and transform from point to rect in the dao
        paint.setColor(this.color);
        paint.setStrokeWidth(borderThickness);
        paint.setAlpha(this.alpha);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Ignore
    public SpeakingRect(int id, String picName, Point point, int borderThickness, int size, int color, int alpha) {
        this.id = id;
        this.picName = picName;
        this.paint = new Paint();
        this.rect = new Rect();
        this.point = point;
        this.borderThickness = borderThickness;
        this.size = size;
        this.color = color;
        this.alpha = alpha;
        setRectPosition(this.point);
        paint.setColor(this.color);
        paint.setStrokeWidth(this.borderThickness);
        paint.setAlpha(this.alpha);//1-255
        paint.setStyle(Paint.Style.STROKE);
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

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    /**
     * 1-10
     * @param thickness
     */
    public void setBorderThickness(int thickness){
        this.borderThickness = thickness;
    }

    public int getBorderThickness(){
        return borderThickness;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Color.RED (example)
     * @param color
     */
    public void setColor(int color){
        this.color = color;
        paint.setColor(this.color);
    }

    public int getColor(){
        return this.color;
    }


    /**
     * 0-255
     * @param alpha
     */
    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setRectPosition(Point point){
        rect.set (point.x - size, point.y - size, point.x + size, point.y +size);
    }

    /**
     * Checks if the new rect will be inside this one
     * @param point
     * @return
     */
    public boolean isPointInsideRect(Point point){
        return this.rect.contains(point.x,point.y);
    }

    /**
     * Checks if the new rect will be inside of this one
     * @param rect
     * @return
     */
    public boolean isRectInside(SpeakingRect rect){
        return this.rect.contains(rect.getRect());
    }

    /**
     * Is the new rect touching this one
     * @param newPoint
     * @param newSize
     * @return
     */
    public boolean areRectanglesIntersecting(Point newPoint, int newSize){
        if(//TODO check for out of bounds
           this.rect.contains(newPoint.x - newSize, newPoint.y - newSize) &&
           this.rect.contains(newPoint.x + newSize,newPoint.y + newSize)
        ){
            return true;
        } else {
            return false;
        }
    }


}
