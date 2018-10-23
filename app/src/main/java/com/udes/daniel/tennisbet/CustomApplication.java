package com.udes.daniel.tennisbet;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomApplication extends Application {

    private Context context;

    private ArrayList<Match> ListMatchs = new ArrayList<Match>();
    private ArrayList<Match> PrevListMatchs = new ArrayList<Match>();
    private NotificationManager notificationManager;
    private ArrayList<UpdateListMatchsListener> listeners = new ArrayList<UpdateListMatchsListener>();
    private boolean connected = false;
    private boolean serviceAlive = false;

    private double bet;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        this.notificationManager = new NotificationManager(context);

        launchService();
    }

    public double getBet() {
        return bet;
    }

    public void setBet(double bet) {
        this.bet = bet;
    }

    public ArrayList<Match> getListMatchs() {
        return this.ListMatchs;
    }

    public void setListMatchs(ArrayList<Match> listMatchs) {
        this.ListMatchs = listMatchs;
    }

    public void updateListMatchs(ArrayList<Match> newListMatchs) {
        this.PrevListMatchs = this.ListMatchs;
        this.ListMatchs = newListMatchs;
        notificationManager.determineChanges(PrevListMatchs, ListMatchs);
        //notificationManager.triggerSetNotification(ListMatchs.get(0), ListMatchs.get(0).getPlayer_1());

        notifyListeners();
    }

    private void launchService() {
        //Launches service
        Intent intent = new Intent(context, UpdateService.class);
        startService(intent);
        setServiceAlive(true);
        Log.i("SERVICE", "Service launched");
    }

    public void addMatch(Match match) {
        this.ListMatchs.add(match);
    }

    public Match getMatch(int id) {
        return this.ListMatchs.get(id);
    }

    public static ArrayList<Match> createListMatchFromJSon (JSONArray obj) {

        ArrayList<Match> ListMatchs = new ArrayList<Match>();

        if (obj != null) {

            try {
                for (int i = 0 ; i < obj.length(); i++) {

                    JSONObject match_json = obj.getJSONObject(i);

                    Match match = new Match(match_json, i);

                    ListMatchs.add(match);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ListMatchs;
    }

    private void connectionToaster(boolean activeConnection) {
        final Context context = getApplicationContext();
        CharSequence text = "";
        final int duration = Toast.LENGTH_SHORT;

        if (activeConnection) {
            text = "Online";
        } else {
            text = "Offline mode";
        }

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    /*      LISTENERS HANDLING        */

    private void notifyListeners() {
        Log.i("INFO","Notifying UpdateListMatchsListener.");
        for (UpdateListMatchsListener listener : listeners) {
            listener.newListMatchsUpdate(ListMatchs);
        }
    }

    public void addListMatchsListener(UpdateListMatchsListener toAdd) {
        listeners.add(toAdd);
    }

    public void removeListMatchsListener(UpdateListMatchsListener toRemove) {
        listeners.remove(toRemove);
        Log.i("LISTENER", "MatchActivity unsubscribed");
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean change) {
        if (this.connected != change) {
            this.connected = change;
            connectionToaster(change);
        }
        if (connected && !serviceAlive) {
            launchService();
        }
    }

    public boolean isServiceAlive() {
        return serviceAlive;
    }

    public void setServiceAlive(boolean serviceAlive) {
        this.serviceAlive = serviceAlive;
    }
}
