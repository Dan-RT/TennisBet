package com.udes.daniel.tennisbet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class UpdateService extends Service {

    //Cours : Service

    private CustomApplication application;

    static final int DELAY = 6000;
    private boolean runFlag = false;
    private Updater updater;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        this.updater = new Updater();
        this.application = (CustomApplication) getApplication();
        Log.i("CIO", "UpdateService : onCreate !");
        application.setServiceAlive(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        this.updater.start();
        this.runFlag = true;
        Log.i("CIO", "UpdateService : onStartCommand !");
        return  START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.runFlag = false;
        this.updater.interrupt();
        this.updater = null;
        Log.i("CIO", "UpdateService : onDestroy !");
    }

    private class Updater extends Thread {

        public Updater (){
            super("UpdateService - Updater");
        }

        @Override
        public void run(){
            UpdateService updateService = UpdateService.this;

            while (updateService.runFlag){

                Log.i("SERVICE", "Performing query");
                HttpURLConnection urlConnection = null;
                ArrayList<Match> listMatchs = null;
                try{
                    Thread.sleep(DELAY);
                    String urlString = "http://10.0.2.2:3000/parties/";
                    URL url = new URL(urlString);

                    urlConnection = (HttpURLConnection) url.openConnection(); // Open
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream()); // Stream

                    String result = AsyncServerRequest.readStream(in); // Read stream

                    JSONArray matchObj = new JSONArray(result);

                    listMatchs = CustomApplication.createListMatchFromJSon(matchObj);
                    application.updateListMatchs(listMatchs);
                    //application.setConnected(true);
                }
                catch (MalformedURLException | JSONException e) { e.printStackTrace(); }
                catch (IOException e) {
                    //e.printStackTrace();
                } catch (InterruptedException e){
                    //e.printStackTrace();
                    updateService.runFlag = false;
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            //application.setConnected(false);
            application.setServiceAlive(false);
            Log.i("SERVICE", "Service stopped");
        }
    }
}
