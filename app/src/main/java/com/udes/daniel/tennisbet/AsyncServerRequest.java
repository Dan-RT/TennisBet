package com.udes.daniel.tennisbet;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.udes.daniel.tennisbet.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class AsyncServerRequest extends AsyncTask<String, Void, ArrayList<Match>> {

    private AppCompatActivity myActivity;

    public AsyncServerRequest(AppCompatActivity mainActivity) {
        myActivity = mainActivity;
    }

    @Override
    protected ArrayList<Match> doInBackground(String... strings) {

        URL url = null;
        HttpURLConnection urlConnection = null;
        String result = null;
        Log.i("URL", strings[0]);
        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection(); // Open
            InputStream in = new BufferedInputStream(urlConnection.getInputStream()); // Stream

            result = readStream(in); // Read stream
        }
        catch (MalformedURLException e) { e.printStackTrace(); }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        JSONObject obj = null;
        try {
            obj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("DATA RECEIVED", obj.toString());

        return creating_match_data(obj);
    }

    protected void onPostExecute(ArrayList<Match> s) {

        System.out.println(s.toString());

    }

    public static String readStream(InputStream stream) {

        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream), 1000);
        String line = "";

        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public ArrayList<Match> creating_match_data (JSONObject obj) {

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
                    Match match = new Match();

                    JSONObject item = matchs_json.getJSONObject(i);
                    JSONObject match_tmp_json = item.getJSONObject(String.valueOf(i));

                    match.setId(match_tmp_json.getInt("id"));

                    JSONObject player_1_json = (JSONObject) match_tmp_json.get("joueur1");
                    Player player1 = new Player((String)player_1_json.get("prenom"), (String)player_1_json.get("nom"), (int)player_1_json.get("age"), (int) player_1_json.get("rang"), (String)player_1_json.get("pays"));

                    JSONObject player_2_json = (JSONObject) match_tmp_json.get("joueur2");
                    Player player2 = new Player((String)player_2_json.get("prenom"), (String)player_2_json.get("nom"), (int)player_2_json.get("age"), (int) player_2_json.get("rang"), (String)player_2_json.get("pays"));

                    match.setPlayer_1(player1);
                    match.setPlayer_2(player2);

                    match.setPitch(match_tmp_json.getInt("terrain"));
                    match.setTournament_name(match_tmp_json.getString("tournoi"));
                    match.setStart_hour(match_tmp_json.getString("heure_debut"));
                    match.setMatch_time(match_tmp_json.getInt("temps_partie"));
                    match.setServer(match_tmp_json.getInt("serveur"));
                    match.setSpeed_last_serv(match_tmp_json.getInt("vitesse_dernier_service"));
                    match.setNb_exchanges(match_tmp_json.getInt("nombre_coup_dernier_echange"));

                    JSONArray contestation = match_tmp_json.getJSONArray("constestation");
                    match.setContests((int)contestation.get(0), (int)contestation.get(1));


                    JSONObject points_json = match_tmp_json.getJSONObject("pointage");
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

                    match.setPoints(points);
                    ListMatchs.add(match);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ListMatchs;
    }
}
