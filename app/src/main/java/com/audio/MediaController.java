package com.audio;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import androidx.core.app.ActivityCompat;

import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;

public class MediaController {

    private String pathSave = "";
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    public MediaController(Activity activity) {
        verifyRecordPermissions(activity);
    }

    private void verifyRecordPermissions(Activity activity) {//TODO remember permission might not work
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            int REQUEST_PERMISSION_CODE = 1000;
            ActivityCompat.requestPermissions(
                    activity, new String[]{RECORD_AUDIO},
                    REQUEST_PERMISSION_CODE
            );
        }
    }

    public void startRecording(String picName, Rect rect) {
        if (picName == null || rect == null) {
            return;
        }
        pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + picName + rect.left + rect.top + rect.right + rect.bottom + ".3gp";

        setupMediaRecorder();

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stopRecording() {
        mediaRecorder.stop();
    }

    public void playSound() {//TODO make it stop playing the prev sound
        if (mediaPlayer != null) {
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

    private void stopSound() {
        if (mediaPlayer != null) {
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
