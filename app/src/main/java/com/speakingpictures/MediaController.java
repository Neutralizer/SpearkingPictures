package com.speakingpictures;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;

public class MediaController {

    final int REQUEST_PERMISSION_CODE = 1000;

    String pathSave = "";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    public MediaController(Activity activity) {
        //TODO  check permission here
        if(checkPermissionFromDevice(activity)){
            requestPermission(activity);
        }
    }

    public void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                WRITE_EXTERNAL_STORAGE,
                RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);

    }

    public boolean checkPermissionFromDevice(Activity activity) {//TODO combine with main method permission check
        int writeExternalStorageResult = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        int recordAudioResult = ContextCompat.checkSelfPermission(activity, RECORD_AUDIO);

        if (writeExternalStorageResult == PackageManager.PERMISSION_GRANTED && recordAudioResult == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public void startRecording(String picName, Point coord){
        pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + picName + coord.x + coord.y + ".3gp";
        //TODO save them

        setupMediaRecorder();//TODO check where this should be

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void stopRecording(){
        mediaRecorder.stop();
    }

    public void playSound(){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(pathSave);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    public void stopSound(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }



    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }


}
