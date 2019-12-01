package com.speakingpictures;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;

public class MediaController {

    private final int REQUEST_PERMISSION_CODE = 1000;

    private String pathSave = "";
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    public MediaController(Activity activity) {
        //TODO  check permission here
        if(checkPermissionFromDevice(activity)){
            requestPermission(activity);
        }
    }

    private void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                WRITE_EXTERNAL_STORAGE,
                RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);

    }

    private boolean checkPermissionFromDevice(Activity activity) {//TODO combine with main method permission check
        int writeExternalStorageResult = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        int recordAudioResult = ContextCompat.checkSelfPermission(activity, RECORD_AUDIO);

        if (writeExternalStorageResult == PackageManager.PERMISSION_GRANTED && recordAudioResult == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public void startRecording(String picName, Rect rect){
        if(picName == null || rect == null){
            return;
        }
        pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + picName + rect.left + rect.top + rect.right + rect.bottom + ".3gp";
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

    public void playSound(){//TODO make it stop playing the prev sound
        if(mediaPlayer != null){
            stopSound();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(pathSave);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

        //
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopSound();
            }
        });
    }

    private void stopSound(){
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
