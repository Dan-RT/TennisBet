package com.udes.daniel.tennisbet;

import org.json.JSONException;
import org.json.JSONObject;

public class Bet {

    private int idMatch;
    private int player;
    private double amount;
    private boolean notificationTriggered = false;

    public Bet() {

    }

    public Bet(int id, int player, double amount) {
        setIdMatch(id);
        setPlayer(player);
        setAmount(amount);
    }

    public int getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String toJSon() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id_match", idMatch);
            obj.put("id_player", player);
            obj.put("bet_amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj.toString();
    }

    public boolean isNotificationTriggered() {
        return notificationTriggered;
    }

    public void setNotificationTriggered(boolean notificationTriggered) {
        this.notificationTriggered = notificationTriggered;
    }
}
