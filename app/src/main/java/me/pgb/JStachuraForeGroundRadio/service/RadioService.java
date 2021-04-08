package me.pgb.JStachuraForeGroundRadio.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import me.pgb.JStachuraForeGroundRadio.controllers.MediaPlayerHandler;
import me.pgb.JStachuraForeGroundRadio.ui.background_radio.RadioFragment;
import me.pgb.JStachuraForeGroundRadio.ui.dashboard.VisualizerFragment;

import static java.lang.Integer.parseInt;

public class RadioService extends Service {

    private MediaPlayerHandler mediaPlayerHandler;
    private String URL = "http://stream.whus.org:8000/whusfm";
    private int volume;
    private int progress = 100;
    public static boolean isChanging;
    private final String TAG = "_SERVICE";
    private final IBinder binder = new LocalBinder();
    private int counter = 0;
    private MyHandler myHandler;
    private HandlerThread handlerThread;
    private Thread backgroundThread;
    private boolean runningInBackground = false;
    private boolean keepRunning = true;

    public class LocalBinder extends Binder {

        public RadioService getService() {
            Log.i(TAG,"LocalBinder extends Binder");
            return RadioService.this;
        }
    }

    public class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
            mediaPlayerHandler = new MediaPlayerHandler();
            mediaPlayerHandler.setupMediaPlayer(URL);
            Log.i(TAG,"MyHandler extends Handler");
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int startId = msg.arg1;
            Object someObject = msg.obj;

            Log.i(TAG,"msg from Handler: " + someObject.toString());
            String command = someObject.toString();
            if (command.equals("OFF")) {
                keepRunning = false;
                mediaPlayerHandler.pauseMediaPlayer();

                runningInBackground = false;
                try {
                    backgroundThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (command.equals("ON")){
                Log.i(TAG, "running in background: " + String.valueOf(runningInBackground));
                if (!runningInBackground) {
                    Log.i(TAG, "Got here, keep running =" + String.valueOf(keepRunning));
                    runningInBackground = true;
                    keepRunning = true;
                    backgroundThread = new Thread("Background Thread in Foreground") {
                        @Override
                        public void run() {
                            Log.i(TAG, "Got here to shutdownsetup");
                            mediaPlayerHandler.shutdownMediaPlayer();
                            mediaPlayerHandler.setupMediaPlayer(URL);
                            mediaPlayerHandler.setMediaVolume(progress);
                            mediaPlayerHandler.asyncLaunchMediaPlayer();
                            while (keepRunning) {

                                try {
                                    Thread.sleep(500);
                                    counter++;
                                } catch (InterruptedException e) {
                                    // Restore interrupt status.
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }
                    };
                    if (!keepRunning) {
                        try {
                            backgroundThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        backgroundThread.start();
                    }
                }
            }

            else // command is a radio station url, or a number for the volume change. \
            {


                try{
                    int v_progress = Integer.parseInt(command);
                    mediaPlayerHandler.setMediaVolume(v_progress);
                }
                catch (NumberFormatException e){
                    URL = command;
                    if(mediaPlayerHandler.isPlaying()) {
                        isChanging = true;
                        radioOff();

                        radioOn();
                        isChanging = false;
                        RadioFragment.statusView.setText("");
                    }
                }



            }

            //counter++;
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate()");

        handlerThread = new HandlerThread("My Thread", Process.THREAD_PRIORITY_FOREGROUND);

        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        myHandler = new MyHandler(looper);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        handlerThread.quitSafely();
    }

    /*
     *
     * if this service is run as an Intent, and we call startService on the intent
     * then the next code is useful
     *
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG,"onStartCommand");

        if(intent != null) {
            // May not have an Intent is the service was killed and restarted
            // (See STICKY_SERVICE).
            Log.i(TAG,"do stuff in onStartCommand");
        }

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind");

        if(intent != null) { // May not have an Intent is the service was killed and restarted (See STICKY_SERVICE).
            Log.i(TAG,"do stuff in onBind");
        }
        return binder;
    }

    public int getCounter() { /** method for clients */
        Message msg = myHandler.obtainMessage();
        msg.arg1 = 99;
        msg.obj = String.valueOf(counter).toString();
        myHandler.sendMessage(msg);
        return counter;
    }


    public void radioOn(){
        Message msg = myHandler.obtainMessage();
        msg.arg1 = 1;
        msg.obj = "ON";
        myHandler.sendMessage(msg);
        VisualizerFragment.session_id = mediaPlayerHandler.getAudioSessionId();

    }
    public void radioOff(){
        VisualizerFragment.audio_set = false;
        Message msg = myHandler.obtainMessage();
        msg.arg1 = 1;
        msg.obj = "OFF";
        myHandler.sendMessage(msg);
    }
    public boolean isPlaying(){
        return mediaPlayerHandler.isPlaying();
    }

    public String getURL() { return URL; }

    public void changeURL(String url){
        if (!url.equals(URL)) {
            Message msg = myHandler.obtainMessage();
            msg.arg1 = 1;
            msg.obj = url;
            myHandler.sendMessage(msg);
        }
        RadioFragment.statusView.setText("");
    }

    public void setVolume(int prog){
        progress = prog;
        Message msg = myHandler.obtainMessage();
        msg.arg1 = 1;
        msg.obj = String.valueOf(progress);
        myHandler.sendMessage(msg);
    }

    public int getProgress(){ return progress;}
    public boolean isChanging(){ return isChanging;}
    public int getAudioSessionId(){ return mediaPlayerHandler.getAudioSessionId();}


}