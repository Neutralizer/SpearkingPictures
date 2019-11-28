package com.speakingpictures;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.*;
import android.widget.Button;

import androidx.core.app.ActivityCompat;

import com.dao.RectDaoImpl;
import com.listeners.GListener;
import com.rect.SpeakingRect;
import com.speakingpictures.views.CustomImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ImageViewMain extends Activity implements OnTouchListener {


    private static int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;//TODO TEMP
    private static String[] PERMISSIONS_STORAGE = {//TODO TEMP
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    RectDaoImpl rectDaoImpl;
    HoldNotifier holdObj = new HoldNotifier(300);
    CustomImageView customImageView;
    String currentPicName;
    GListener gListener;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        customImageView = new CustomImageView(this);
        rectDaoImpl = new RectDaoImpl(this);//new
        final CustomImageView imgView =  findViewById(R.id.myimageID);
        imgView.setOnTouchListener(this);
        gListener = new GListener(this);

        verifyStoragePermissions(this);//TODO this is first and the button is 2nd, because you are displayed the pics before allowing it- api21, not api23

        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        //TODO

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gListener.getGestureDetector().onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void showImage() {

        final CustomImageView imgView =  findViewById(R.id.myimageID);
        imgView.post(new Runnable() {//TODO protip - can't access db from this thread
            @Override
            public void run() {
//                imgView.setImageResource(mImageIds[image_index]);//TODO old img loading
                @SuppressLint({"NewApi", "LocalSuppress"}) int maxWidth = imgView.getWidth();
                @SuppressLint({"NewApi", "LocalSuppress"}) int maxHeight = imgView.getHeight();
                System.out.println("max: " + maxWidth + " " + maxHeight );



            }
        });

        setRectanglesInsideCustomView(imgView);

        System.out.println("image shown");//TODO



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

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if(currentPicName == null){ return false; }//block touch events when no pic is loaded

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: //click
                //TODO start recording in new thread
                //TODO tell visually with red hue that it is recording
                //TODO get pic coords and create button/save button //rect
                System.out.println("action down");//TODO
                holdObj.isPressed();

                final Point imageViewClickPosition = customImageView.getImageViewClickPosition(motionEvent);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(holdObj.getHoldDuration());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (holdObj.getIsPressed()) {
                            System.out.println("it is a hold");
                            holdObj.setHeld(true);
                            //TODO execute hold code
                            final CustomImageView imgView = findViewById(R.id.myimageID);//TODO make this the only 1?
                            rectDaoImpl.insertRect(currentPicName,imageViewClickPosition);


                            setRectanglesInsideCustomView(imgView);

                            //TODO start audio recording




                        }

                    }
                }).start();





                return true;
            case MotionEvent.ACTION_UP: //release

                if(holdObj.isHeld()){
                    //TODO release a hold
                }
                //TODO stop the recording thread and save audiofile //TODO save button coordinates and audio

                //TODO update prop file
                System.out.println("action up");//TODO
                holdObj.holdReleased();//TODO should be released in any case


                return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            CustomImageView imageView = findViewById(R.id.myimageID);//TODO was imageview
            File file = new File(picturePath);
            currentPicName = file.getName();
            Picasso.with(getApplicationContext()).load(file).fit().centerInside().into(imageView);

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            setRectanglesInsideCustomView(imageView);


        }


    }


    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {//TODO check for other permissions also //TODO move to separate class
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);//WAS WRITE TODO

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


}