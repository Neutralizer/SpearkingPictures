package com.speakingpictures.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.rect.SpeakingRect;
import com.speakingpictures.R;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("AppCompatCustomView")
public class CustomImageView extends ImageView implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{
    GestureDetector gestureDetector = new GestureDetector(getContext(),this);
//    {
//        gestureDetector.setOnDoubleTapListener(this);
//    }

    List<SpeakingRect> rectList = new ArrayList<>();

    public void setRectList(List<SpeakingRect> list){
        this.rectList = list;
    }

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public Point getImageViewClickPosition(MotionEvent motionEvent) {
        //get image loc
        int[] posXY = new int[2];
        this.getLocationOnScreen(posXY);

        int touchX = (int) motionEvent.getX();
        int touchY = (int) motionEvent.getY();
        System.out.println("X - " + touchX + " Y - " + touchY);//TODO
        return new Point(touchX,touchY);

    }

    @Override
    public void onDraw(Canvas canvas) {//TODO just display the rects of the pic
        super.onDraw(canvas);

            for (SpeakingRect rect : rectList){
                canvas.drawRect(rect.getRect().left, rect.getRect().top, rect.getRect().right, rect.getRect().bottom, rect.getPaint());
            }
//        }
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {//TODO detects outside of view where point is blank
        System.out.println("doubletapping");
        final Point imageViewClickPosition = getImageViewClickPosition(motionEvent);
        final CustomImageView imgView =  findViewById(R.id.myimageID);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                rectDaoImpl.deleteRect(currentPicName,imageViewClickPosition);
                Log.d("glistener", "it is doubletapping");
//                setRectanglesInsideCustomView(imgView);
            }
        }).start();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        System.out.println("longpressing");
        final Point imageViewClickPosition = getImageViewClickPosition(motionEvent);
        final CustomImageView imgView =  findViewById(R.id.myimageID);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                rectDaoImpl.insertRect(currentPicName,imageViewClickPosition);
                Log.d("onlongpress", "it is longpressing");
//                setRectanglesInsideCustomView(imgView);
            }
        }).start();

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gestureDetector.onTouchEvent(event)){
            return true;//does not trigger action_up without this
        }

        if(event.getAction() == MotionEvent.ACTION_UP){
            System.out.println("RELEASE THE CKRACKCEN");
        }


        return super.onTouchEvent(event);
    }
}