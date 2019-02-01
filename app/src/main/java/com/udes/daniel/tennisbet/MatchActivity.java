package com.udes.daniel.tennisbet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MatchActivity extends AppCompatActivity implements UpdateListMatchsListener {

    private int id;
    private CustomApplication application;
    private MatchActivity current_activity;
    private Match match;
    private TextView text_view_time;

    private TextView text_view_player_1;
    private TextView text_view_sets_player_1;
    private TextView text_view_games_player_1;
    private TextView text_view_points_player_1;
    private TextView text_view_contest_player_1;

    private TextView text_view_player_2;
    private TextView text_view_sets_player_2;
    private TextView text_view_games_player_2;
    private TextView text_view_points_player_2;
    private TextView text_view_contest_player_2;

    private RadioButton radio_button_bet_player_1;
    private RadioButton radio_button_bet_player_2;
    private RadioGroup radio_group_players;
    private Button match_bet_button;
    private EditText result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        this.application = (CustomApplication) getApplication();
        this.application.addListMatchsListener(this);

        setContentView(R.layout.activity_match);
        current_activity = this;

        final Intent i = getIntent();
        match = new Match();
        match.setId(i.getIntExtra("id_match",0));
        this.id = match.getId();

        this.setElementView();
        refresh_data();



        match_bet_button = (Button) findViewById(R.id.activity_match_bet_button);
        Bet bet = null;
        if (match != null) {
            bet = application.getBetMatch(id);
            Toast toast = Toast.makeText(context, "id : " +id, Toast.LENGTH_SHORT);
            toast.show();
            if (bet != null) {
                manageBetButton(match.getPlayerNameById(bet.getPlayer()),bet.getAmount());
            }
        }

        //source : https://www.youtube.com/watch?v=oHW6_VP7_oM&feature=share
        match_bet_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.betting_dialog,null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.bet_value);
                radio_group_players = (RadioGroup) promptsView.findViewById(R.id.radio_player);
                radio_button_bet_player_1 = (RadioButton) promptsView.findViewById(R.id.activity_bet_name_player_1);
                radio_button_bet_player_2 = (RadioButton) promptsView.findViewById(R.id.activity_bet_name_player_2);

                radio_button_bet_player_1.setText(text_view_player_1.getText().toString());
                radio_button_bet_player_2.setText(text_view_player_2.getText().toString());
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Bet",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        int id_player = 0;
                                        LayoutInflater l = LayoutInflater.from(context);
                                        View ptView = l.inflate(R.layout.betting_dialog, null);
                                        // get user input and set it to result
                                        // edit text
                                        int selectedId = radio_group_players.getCheckedRadioButtonId();
                                        RadioButton PlayerButton = (RadioButton) ptView.findViewById(selectedId);

                                        if (selectedId == radio_button_bet_player_1.getId()) {
                                            id_player = 1;
                                        } else if(selectedId == radio_button_bet_player_2.getId()){
                                            id_player = 2;
                                        }

                                        placeBet(match.getId(), id_player, Double.parseDouble(userInput.getText().toString()));

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                // alertDialog.getWindow().setBackgroundDrawableResource();

            }
        });
    }

    private void setElementView () {
        text_view_time = findViewById(R.id.activity_match_time);
        text_view_player_1 = (TextView) findViewById(R.id.activity_match_name_player_1);
        text_view_sets_player_1 = findViewById(R.id.activity_match_sets_player_1);
        text_view_games_player_1 = findViewById(R.id.activity_match_games_player_1);
        text_view_points_player_1 = findViewById(R.id.activity_match_points_player_1);

        text_view_player_2 = (TextView) findViewById(R.id.activity_match_name_player_2);
        text_view_sets_player_2 = findViewById(R.id.activity_match_sets_player_2);
        text_view_games_player_2 = findViewById(R.id.activity_match_games_player_2);
        text_view_points_player_2 = findViewById(R.id.activity_match_points_player_2);

        Button refresh_button = findViewById(R.id.activity_match_refresh_button);
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh_data();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        application.removeListMatchsListener(this);
        Log.i("CIO", "MatchActivity : onDestroy !");
    }

    private void refresh_data () {
        retrieve_data();
        update_UI();
    }

    private void retrieve_data () {
        AsyncServerRequest request = new AsyncServerRequest(current_activity);

        ArrayList<Match> tmp =  new ArrayList<Match>();
        String URL = "http://10.0.2.2:3000/parties/" + match.getId();

        try {
            tmp = request.execute(URL).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (tmp != null) {
            application.setConnected(true);
            match = tmp.get(0);
        } else {
            application.setConnected(false);
        }
    }



    private void update_UI(){
        if (match != null) {
            text_view_time.setText(this.getTime(match.getMatch_time()));

            text_view_player_1.setText(match.getPlayer_1().getFirst_name() + " " + match.getPlayer_1().getSurname());
            text_view_player_2.setText(match.getPlayer_2().getFirst_name() + " " + match.getPlayer_2().getSurname());

            Points points = match.getPoints();

            text_view_sets_player_1.setText(points.getSets().get(0).toString());
            text_view_sets_player_2.setText(points.getSets().get(1).toString());

            int numSet;
            switch (points.getGames().size()){
                case 1:
                    numSet = 0;
                    break;
                case 2:
                    numSet = 1;
                    break;
                case 3:
                    numSet = 2;
                    break;
                default:
                    numSet = 0;
            }

            text_view_games_player_1.setText(points.getGames().get(numSet).get(0).toString());
            text_view_games_player_2.setText(points.getGames().get(numSet).get(1).toString());

            text_view_points_player_1.setText(points.getExchange().get(0).toString());
            text_view_points_player_2.setText(points.getExchange().get(1).toString());

            text_view_points_player_1.setText(String.valueOf(this.getScore(points.getExchange().get(0))));
            text_view_points_player_2.setText(String.valueOf(this.getScore(points.getExchange().get(1))));

        }
    }

    @Override
    public void newListMatchsUpdate(ArrayList<Match> ListMatchs) {
        Log.i("LISTENER", "New data received");
        Log.i("LISTENER", ListMatchs.get(this.id).toString());
        this.match = ListMatchs.get(this.id);

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    update_UI();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void placeBet(int id_match, int id_player, double amount) {

        final Bet bet = new Bet(id_match, id_player, amount);

        new AsyncTaskPOSTrequest().execute("http://10.0.2.2:3000/parties/pari/", bet.toJSon());

        application.addBetMatch(bet);
        manageBetButton(match.getPlayerNameById(id_player), amount);
    }

    private void manageBetButton(String player, double amount) {
        ((TextView) findViewById(R.id.display_bet)).setText("You bet " + amount + "$ for " + player);
        match_bet_button.setEnabled(false);
    }

    private int getScore (int score) {
        switch (score){
            case 0 :
                return 0;
            case 1 :
                return 15;
            case 2 :
                return 30;
            case 3 :
                return 40;
            default :
                return 0;
        }
    }

    private String getTime (int totalSecond) {
        int hours = (int) totalSecond / 3600;
        int minutes = (totalSecond % 3600) / 60;
        int seconds = totalSecond - minutes * 60 - hours * 3600;

        return hours + "h " + minutes + "min " + seconds + "s";
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //https://developer.android.com/guide/topics/resources/runtime-changes#HandlingTheChange
        super.onConfigurationChanged(newConfig);

        Log.i("CIO", "Match Activity : Configuration changed ! ");
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_match_land);
            this.setElementView();
            this.refresh_data();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_match);
            this.setElementView();
            this.refresh_data();
        }
    }
}