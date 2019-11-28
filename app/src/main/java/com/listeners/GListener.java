package com.listeners;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.dao.RectDaoImpl;
import com.rect.SpeakingRect;
import com.speakingpictures.views.CustomImageView;

import java.util.List;

public class GListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    GestureDetector gestureDetector;//TODO access db from here, and audio
    RectDaoImpl rectDaoImpl;
    private CustomImageView imgView;
    String currentPicName = null;

    public GListener(Context context, RectDaoImpl rectDaoImp, CustomImageView imgView){
        gestureDetector = new GestureDetector(context, this);
        this.rectDaoImpl = rectDaoImp;
        this.imgView = imgView;
    }

    private void setRectanglesInsideCustomView(final CustomImageView imgView) {//TODO make CustomImageView internal - it will not change (maybe with the gridview adapter)
        new Thread(new Runnable() {
            @Override
            public void run() {

//                rectDaoImpl.deleteAll();//TODO clear all
                List<SpeakingRect> allRects = rectDaoImpl.getAllRects();
                System.out.println(allRects.size() + "sizeeeeeeeeeeeeeeeeeee");//TODO
                for(SpeakingRect rect : allRects){
                    System.out.println("id and rectname and rect loc--- "  + rect.getId()+ " " +  rect.getPicName() + " " + rect.getRect().bottom + " is bottom");
                }
                //TODO update with view.post //TODO on a new thread get the db data then update it to view with post :) - may switch the operations - update 1st,get db 2nd
                List<SpeakingRect> caters = rectDaoImpl.getAllRectsOfAPic(currentPicName);
                imgView.setRectList(caters);
                imgView.postInvalidate();
            }

        }).start();
    }


    public void setCurrentPicName(String picName){
        this.currentPicName = picName;
    }

    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        System.out.println("doubletapping");
        final Point imageViewClickPosition = imgView.getImageViewClickPosition(e);
        new Thread(new Runnable() {
            @Override
            public void run() {
        rectDaoImpl.deleteRect(currentPicName,imageViewClickPosition);
                Log.d("glistener", "it is doubletapping");
            }
        }).start();
        setRectanglesInsideCustomView(imgView);
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        System.out.println("longpressing");
        final Point imageViewClickPosition = imgView.getImageViewClickPosition(e);
        new Thread(new Runnable() {
            @Override
            public void run() {
        rectDaoImpl.insertRect(currentPicName,imageViewClickPosition);
                Log.d("onlongpress", "it is longpressing");

            }
        }).start();
        setRectanglesInsideCustomView(imgView);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

}
