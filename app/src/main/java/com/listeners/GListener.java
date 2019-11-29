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

    public GListener(Context context, RectDaoImpl rectDaoImp, CustomImageView imgView){
        gestureDetector = new GestureDetector(context, this);
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
//        System.out.println("doubletapping");
//        final Point imageViewClickPosition = imgView.getImageViewClickPosition(e);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//        rectDaoImpl.deleteRect(currentPicName,imageViewClickPosition);
//                Log.d("glistener", "it is doubletapping");
//            }
//        }).start();
//        setRectanglesInsideCustomView(imgView);
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
//        System.out.println("longpressing");
//        final Point imageViewClickPosition = imgView.getImageViewClickPosition(e);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//        rectDaoImpl.insertRect(currentPicName,imageViewClickPosition);
//                Log.d("onlongpress", "it is longpressing");
//
//            }
//        }).start();
//        setRectanglesInsideCustomView(imgView);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

}
