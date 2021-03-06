package com.dao;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.rect.SpeakingRect;

import java.util.ArrayList;
import java.util.List;

/**
 * Instantiating constructor must be called for 2 arg db storage
 * //TODO try to fix the explicit constructor call - may not be designed this way - paint
 * //TODO and rect are not persisted and the constructor needs to be called explicitly = force all fields to be persisted?
 */
public class RectDaoImpl {

    private  RectDatabase appDb;//TODo null checks? - only 1 for now

    public RectDaoImpl(Context context){
        appDb = (RectDatabase) RectDatabase.getInstance(context);
    }

    public List<SpeakingRect> getAllRects(){
        List<SpeakingRect> allRectsFromDB = appDb.rectDao().getAllRects();
        List<SpeakingRect> allRectsOfAPicResult = new ArrayList<>();
        for (SpeakingRect rect : allRectsFromDB){
            allRectsOfAPicResult.add(new SpeakingRect(rect.getId(),rect.getPicName(),rect.getRect(),rect.getPaint()));
        }
        return allRectsOfAPicResult;
    }

    /**
     * System.out.println("Alpha " + caters.get(0).getAlpha() + " Alpha from paint " +caters.get(0).getPaint().getAlpha() );
     * //TODO due to room persistence not able to hold objects, constructors that initialize default values must be called - not anymore - change code if not
     * @param picName
     * @return
     */
    public List<SpeakingRect> getAllRectsOfAPic(String picName){
        List<SpeakingRect> allRectsOfAPicFromDB = appDb.rectDao().getAllRectsOfAPic(picName);
        List<SpeakingRect> allRectsOfAPicResult = new ArrayList<>();
        for (SpeakingRect rect : allRectsOfAPicFromDB){
            allRectsOfAPicResult.add(new SpeakingRect(rect.getId(),rect.getPicName(),rect.getRect(),rect.getPaint()));
        }
        return allRectsOfAPicResult;

    }

    public SpeakingRect getRect(String picName, Rect rect){
        return appDb.rectDao().getRect(picName, rect);
    }

    public SpeakingRect getRect(String picName, Point point){
        List<SpeakingRect> allRectsOfAPic = getAllRectsOfAPic(picName);
        for(SpeakingRect rectangle : allRectsOfAPic){
            if (rectangle.getRect().contains(point.x, point.y)) {
                return rectangle;
            }
        }

        return null;
    }

    public SpeakingRect insertRect(String picName, Point point){ // id is autogenerated - pass 0 as not set
        if(picName != null){
            Paint paint = new Paint();//default paint
            paint.setStyle(Paint.Style.STROKE);//not inserted into db
            paint.setColor(Color.RED);
            paint.setStrokeWidth(3);
            paint.setAlpha(100);
            Rect rect = createRectfromPoint(point, 50);
            SpeakingRect speakingRect = new SpeakingRect(0,picName,rect,paint);//call constructor for default values
            appDb.rectDao().insertRect(speakingRect);
            return speakingRect;
        }
        return null;
    }

    //TODO basic update only - picName and point
    public void updateRect(int id, String picName, Rect rect){
        appDb.rectDao().updateRect(id, picName, rect);
    }

    public void deleteAllRectsOfAPic(String picName){
        appDb.rectDao().deleteAllRectsOfAPic(picName);
    }


    /**
     *
     * @param picName picture name to which this rect belongs
     * @param point the point that is clicked - if there is a rectangle there - it will be deleted
     * @return the deleted speakingRect
     */
    public SpeakingRect deleteRect(String picName, Point point){
        List<SpeakingRect> allRectsOfAPic = getAllRectsOfAPic(picName);
        for(SpeakingRect rectangle : allRectsOfAPic){
            if (rectangle.getRect().contains(point.x, point.y)) {
                appDb.rectDao().deleteRect(picName, rectangle.getRect());
                return rectangle;
            }
        }

        return null;
    }

    public void deleteAll(){
        appDb.rectDao().deleteAll();
    }

    public Rect createRectfromPoint(Point point, int size){
        Rect rect = new Rect();
        rect.set (point.x - size, point.y - size, point.x + size, point.y +size);
        return rect;
    }

}
