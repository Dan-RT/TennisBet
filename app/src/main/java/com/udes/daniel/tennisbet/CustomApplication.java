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
}
