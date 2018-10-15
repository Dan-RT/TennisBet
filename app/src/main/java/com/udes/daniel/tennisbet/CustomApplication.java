package com.udes.daniel.tennisbet;

import android.app.Application;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomApplication extends Application {

    private ArrayList<Match> ListMatchs = new ArrayList<Match>();
    private double bet;

    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
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

    public void updateMatch(Match match) {
        for (Match prevMatch:ListMatchs) {
            if (prevMatch.getId() == match.getId()) {
                prevMatch = match;
                //si jamais y'a un bug c'est ici
            }
        }
    }

    public void addMatch(Match match) {
        this.ListMatchs.add(match);
    }

    public Match getMatch(int id) {
        return this.ListMatchs.get(id);
    }

    public static ArrayList<Match> createListMatchFromJSon (JSONObject obj) {

        JSONArray matchs_json = null;
        ArrayList<Match> ListMatchs = new ArrayList<Match>();

        try {
            matchs_json = (JSONArray) obj.get("matchs");
            Log.i("matchs_json : ", matchs_json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (matchs_json != null) {

            try {
                for (int i = 0 ; i < matchs_json.length(); i++) {

                    JSONObject ListMatchsJSon = matchs_json.getJSONObject(i);

                    JSONObject match_json = ListMatchsJSon.getJSONObject(String.valueOf(i));
                    Match match = new Match(match_json);

                    ListMatchs.add(match);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ListMatchs;
    }
}
