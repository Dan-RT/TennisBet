package com.udes.daniel.tennisbet;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Match implements Parcelable {
    private int id;
    private Player player_1;
    private Player player_2;
    private String tournament_name;
    private int pitch;
    private String start_hour;
    private Points points;
    private int match_time;
    private int server;
    private int speed_last_serv;;
    private int nb_exchanges;
    private ArrayList<Integer> contests;

    public Match () {
        this.contests = new ArrayList<Integer>();
        this.player_1 = new Player();
        this.player_2 = new Player();
    }

    public Match (int id, Player player_1, Player player_2, String tournament_name, int pitch, String start_hour, Points points, int match_time, int server, int speed_last_serv, int nb_exchanges, int contest_p_1, int contest_p_2) {
        this.contests = new ArrayList<Integer>();
        this.id = id;
        this.player_1 = player_1;
        this.player_2 = player_2;
        this.tournament_name = tournament_name;
        this.pitch = pitch;
        this.start_hour = start_hour;
        this.points = points;
        this.match_time = match_time;
        this.server = server;
        this.speed_last_serv = speed_last_serv;
        this.nb_exchanges = nb_exchanges;

        this.contests.add(contest_p_1);
        this.contests.add(contest_p_2);
    }

    public Match (JSONObject match_json, int id) {

        try {
            this.setId(id);

            JSONObject player_1_json = (JSONObject) match_json.get("joueur1");
            Player player1 = new Player((String)player_1_json.get("prenom"), (String)player_1_json.get("nom"), (int)player_1_json.get("age"), (int) player_1_json.get("rang"), (String)player_1_json.get("pays"));

            JSONObject player_2_json = (JSONObject) match_json.get("joueur2");
            Player player2 = new Player((String)player_2_json.get("prenom"), (String)player_2_json.get("nom"), (int)player_2_json.get("age"), (int) player_2_json.get("rang"), (String)player_2_json.get("pays"));

            this.setPlayer_1(player1);
            this.setPlayer_2(player2);

            this.setPitch(match_json.getInt("terrain"));
            this.setTournament_name(match_json.getString("tournoi"));
            this.setStart_hour(match_json.getString("heure_debut"));
            this.setMatch_time(match_json.getInt("temps_partie"));
            this.setServer(match_json.getInt("serveur"));
            this.setSpeed_last_serv(match_json.getInt("vitesse_dernier_service"));
            this.setNb_exchanges(match_json.getInt("nombre_coup_dernier_echange"));

            JSONArray contestation = match_json.getJSONArray("constestation");
            this.setContests((int)contestation.get(0), (int)contestation.get(1));


            JSONObject points_json = match_json.getJSONObject("pointage");
            JSONArray sets_json = points_json.getJSONArray("manches");

            JSONArray games_json = points_json.getJSONArray("jeu");
            ArrayList<ArrayList<Integer>> games = new ArrayList<ArrayList<Integer>>();

            for (int j = 0; j < games_json.length(); j++) {
                ArrayList<Integer> game = new ArrayList<Integer>();
                JSONArray game_json = (JSONArray) games_json.get(j);
                for (int k = 0; k < game_json.length(); k++) {
                    game.add(game_json.getInt(k));
                }
                games.add(game);
            }

            JSONArray exchange_json = points_json.getJSONArray("echange");
            boolean final_ = points_json.getBoolean("final");
            Points points = new Points(sets_json.getInt(0), sets_json.getInt(1), games, exchange_json.getInt(0), exchange_json.getInt(1), final_);

            this.setPoints(points);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected Match(Parcel in) {
        id = in.readInt();
        tournament_name = in.readString();
        pitch = in.readInt();
        start_hour = in.readString();
        match_time = in.readInt();
        server = in.readInt();
        speed_last_serv = in.readInt();
        nb_exchanges = in.readInt();
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getPlayer_1() {
        return player_1;
    }

    public void setPlayer_1(Player player_1) {
        this.player_1 = player_1;
    }

    public Player getPlayer_2() {
        return player_2;
    }

    public void setPlayer_2(Player player_2) {
        this.player_2 = player_2;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public String getStart_hour() {
        return start_hour;
    }

    public void setStart_hour(String start_hour) {
        this.start_hour = start_hour;
    }

    public Points getPoints() {
        return points;
    }

    public void setPoints(Points points) {
        this.points = points;
    }

    public int getMatch_time() {
        return match_time;
    }

    public void setMatch_time(int match_time) {
        this.match_time = match_time;
    }

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public int getSpeed_last_serv() {
        return speed_last_serv;
    }

    public void setSpeed_last_serv(int speed_last_serv) {
        this.speed_last_serv = speed_last_serv;
    }

    public int getNb_exchanges() {
        return nb_exchanges;
    }

    public void setNb_exchanges(int nb_exchanges) {
        this.nb_exchanges = nb_exchanges;
    }

    public ArrayList<Integer> getContests() {
        return contests;
    }

    public void setContests(ArrayList<Integer> contests) {
        this.contests = contests;
    }

    public void setContests(int contests_p_1, int contests_p_2) {
        if(this.contests == null){
            this.contests = new ArrayList<Integer>();
        }
        this.contests.add(contests_p_1);
        this.contests.add(contests_p_2);
    }

    public String getTournament_name() {
        return tournament_name;
    }

    public void setTournament_name(String tournament_name) {
        this.tournament_name = tournament_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(tournament_name);
        parcel.writeInt(pitch);
        parcel.writeString(start_hour);
        parcel.writeInt(match_time);
        parcel.writeInt(server);
        parcel.writeInt(speed_last_serv);
        parcel.writeInt(nb_exchanges);
        parcel.writeTypedObject(player_1, 1);
    }

    @Override
    public String toString() {
        try {
            return  tournament_name + " - " + start_hour + "\n" + player_1.toString() + " - " + player_2.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
