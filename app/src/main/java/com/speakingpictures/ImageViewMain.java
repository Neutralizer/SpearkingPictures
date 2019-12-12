package com.speakingpictures;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.audio.MediaController;
import com.dao.RectDaoImpl;
import com.rect.SpeakingRect;
import com.speakingpictures.views.CustomImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ImageViewMain extends Activity implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {


    private static int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    RectDaoImpl rectDaoImpl;
    HoldNotifier holdObj = new HoldNotifier(300);
    CustomImageView customImageViewObj;
    String currentPicName;
    GestureDetector gestureDetector;
    MediaController mediaController;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyPermissions(this);//TODO this is first and the button is 2nd, because you are displayed the pics before allowing it- api21, not api23
        //TODO permission problem
        customImageViewObj = new CustomImageView(this);
        rectDaoImpl = new RectDaoImpl(this);//new
        final CustomImageView imgView =  findViewById(R.id.myimageID);
        gestureDetector = new GestureDetector(this,this);
        mediaController = new MediaController(this);

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

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        //play //TODO get the touched rect and input its coords
        //TODO in delete method the touched rect is selected - get it the same way - maybe save it by 4 sides, not by point
        final Point imageViewClickPosition = customImageViewObj.getImageViewClickPosition(motionEvent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Rect rect = rectDaoImpl.getRect(currentPicName, imageViewClickPosition);
                mediaController.playSound(currentPicName,rect); // TODO start playing in new thread and when it is finished - stop playing from that thread - may not be able to stop

            }
        }).start();
        return true;//get the touched rect and give its coords to the mediacontroller to play this spesific rect's sounds
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {//TODO detects outside of view where point is blank
        System.out.println("doubletapping");
        final Point imageViewClickPosition = customImageViewObj.getImageViewClickPosition(motionEvent);
        final CustomImageView imgView =  findViewById(R.id.myimageID);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Rect rect = rectDaoImpl.deleteRect(currentPicName, imageViewClickPosition);
                Log.d("glistener", "it is doubletapping");
                setRectanglesInsideCustomView(imgView);
                //TODO delete audiofile when deleting rect - check if it works
                mediaController.deleteAudioFile(currentPicName,rect);

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
        //TODO start recording in new thread
        //TODO tell visually with red hue that it is recording
        //get pic coords and create button/save button //rect
        System.out.println("longpressing");
        holdObj.setHeld(true);
        final Point imageViewClickPosition = customImageViewObj.getImageViewClickPosition(motionEvent);
        final CustomImageView imgView =  findViewById(R.id.myimageID);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SpeakingRect speakingRect = rectDaoImpl.insertRect(currentPicName,imageViewClickPosition);
                Log.d("onlongpress", "it is longpressing");
                setRectanglesInsideCustomView(imgView);

                mediaController.startRecording(currentPicName,speakingRect.getRect());
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

        if(event.getAction() == MotionEvent.ACTION_UP && holdObj.isHeld()){//TODO use holdObj here to stop recording only after longpress
            System.out.println("RELEASE THE CKRACKCEN");//TODO
            //TODO stop the recording thread and save audiofile //TODO save button coordinates and audio
            mediaController.stopRecording();
            holdObj.setHeld(false);
        }
        return super.onTouchEvent(event);
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
                List<SpeakingRect> list = rectDaoImpl.getAllRectsOfAPic(currentPicName);
                imgView.setRectList(list);
                imgView.postInvalidate();
            }

        }).start();
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


            CustomImageView imageView = findViewById(R.id.myimageID);
            File file = new File(picturePath);
            currentPicName = file.getName();
            Picasso.with(getApplicationContext()).load(file).fit().centerInside().into(imageView);

//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));//not needed //init
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
    public static void verifyPermissions(Activity activity) {//TODO move to permissionManager class
        // Check if we have permission
        int permissionStorage = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionAudio = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);


        if (permissionStorage != PackageManager.PERMISSION_GRANTED && permissionAudio != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
            new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
            },
                    REQUEST_EXTERNAL_STORAGE
            );
        } else if(permissionStorage != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    activity,
                    new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    REQUEST_EXTERNAL_STORAGE
            );
        } else if(permissionAudio != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    activity,
                    new String[] {
                            Manifest.permission.RECORD_AUDIO
                    },
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


}