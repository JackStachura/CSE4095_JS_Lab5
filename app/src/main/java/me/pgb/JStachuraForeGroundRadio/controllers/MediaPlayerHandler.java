package me.pgb.JStachuraForeGroundRadio.controllers;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class MediaPlayerHandler {

    private final String TAG = "_MEDIA_";

    private MediaPlayer mediaPlayer;

    public MediaPlayerHandler() {
        mediaPlayer = new MediaPlayer();
    }

    public void createMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
    }

    public void setupMediaPlayer(String streamLink) {
        Log.i(TAG, "setupMediaPlayerDetails");

        if (mediaPlayer != null) {
            //mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG, "mediaPlayer.start()");
                mediaPlayer.start();
            }
        });

        try {
            mediaPlayer.setDataSource(streamLink);
        } catch (IOException e) {
            Log.i(TAG, "Exception in setupMediaPlayer");
            e.printStackTrace();
        }
    }

    public void asyncLaunchMediaPlayer() {
        try {
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.i(TAG, "Exception in asyncLaunchMediaPlayerOnLink");
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

    public void pauseMediaPlayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void shutdownMediaPlayer() {
        Log.i(TAG, "shutdownMediaPlayer()");
        if (mediaPlayer != null) { // && mediaPlayer.isPlaying()
            Log.i(TAG, "Stop, release, shutdownMediaPlayer()");
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        mediaPlayer = null;
    }

    public void setMediaVolume(int prog){
        Log.i("VOLUME", " " + String.valueOf(prog));
        float log1=(float)( 1 - (Math.log(100- prog)/Math.log(100)));
        mediaPlayer.setVolume(log1, log1);
    }
    public int getAudioSessionId(){
        //Returns the audio session ID of the mediaPlayer.
        return mediaPlayer.getAudioSessionId();
    }
}

