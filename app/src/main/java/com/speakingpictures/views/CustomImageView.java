package com.speakingpictures.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.rect.SpeakingRect;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("AppCompatCustomView")
public class CustomImageView extends ImageView {

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

//        if(manipulator != null){
//            ArrayList<SpeakingRect> pictureRectangles = manipulator.getRectangles("caters");//TODO picName is hardcoded
            for (SpeakingRect rect : rectList){
                canvas.drawRect(rect.getRect().left, rect.getRect().top, rect.getRect().right, rect.getRect().bottom, rect.getPaint());
            }
//        }
    }
}