package com.udes.daniel.tennisbet;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.application = (CustomApplication) getApplication();
        this.application.addListMatchsListener(this);

        setContentView(R.layout.activity_match);
        current_activity = this;

        Intent i = getIntent();
        match = new Match();
        match.setId(i.getIntExtra("id_match",0));
        this.id = match.getId();

        match = application.getMatch(match.getId());

        this.setElementView();
        refresh_data();

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
            //text_view_time.setText(Integer.toString(match.getMatch_time()));
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
}
