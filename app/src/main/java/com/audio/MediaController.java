package com.audio;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;

public class MediaController {

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    public MediaController(Activity activity) {
        createFolderIfNotExisting();

//        verifyRecordPermissions(activity);
    }



    private void verifyRecordPermissions(Activity activity) {
        // Check if we have mic permission
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
        String pathForSave = reconstructPathSave(picName, rect);

        setupMediaRecorder(pathForSave);

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

    private void createFolderIfNotExisting() {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "SpeakingPictures");
        if(!file.exists() || !file.isDirectory()){//TODO isDir checks if it exists actually
            //create the dir or stop the program
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                try {
                    throw new IOException("Could not create dir" + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String reconstructPathSave(String picName, Rect rect){
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "SpeakingPictures" + File.separator +
                picName + rect.left + rect.top + rect.right + rect.bottom + ".3gp";

    }

    public void playSound(String picName, Rect rect) {//TODO pass the audio save location
        if (picName == null || rect == null) {
            return;
        }

//        if (mediaPlayer != null) {
//            stopSound();
//            mediaPlayer = null;//TODO user stops other playing sound, but wants to play this one now - test
//        }

        mediaPlayer = new MediaPlayer();//TODO don't recreate mp on every sound
        try {
            String pathForSave = reconstructPathSave(picName, rect);

            mediaPlayer.setDataSource(pathForSave);//TODO this will work only for 1 file - not anymore
            System.out.println(pathForSave);//TODO test
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

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


    private void setupMediaRecorder(String pathForSave) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathForSave);
    }


    public void deleteAudioFile(String picName, Rect rect) {
        if (picName == null || rect == null) {
            return;
        }

        String pathForSave = reconstructPathSave(picName, rect);
        File file = new File(pathForSave);
        boolean delete = file.delete();
        if (!delete){
            try {
                throw new IOException("Could not delete " + file.getName() + " located in " + pathForSave);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
