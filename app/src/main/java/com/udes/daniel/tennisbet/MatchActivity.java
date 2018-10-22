package com.udes.daniel.tennisbet;

import android.content.Intent;
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

public class MatchActivity extends AppCompatActivity {

    private MatchActivity current_activity;
    private Match match;
    private TextView text_view_time;
    private TextView text_view_player_1;
    private TextView text_view_sets_player_1;
    private TextView text_view_games_player_1;
    private TextView text_view_points_player_1;

    private TextView text_view_player_2;
    private TextView text_view_sets_player_2;
    private TextView text_view_games_player_2;
    private TextView text_view_points_player_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        current_activity = this;

        text_view_time = findViewById(R.id.activity_match_time);

        text_view_player_1 = (TextView) findViewById(R.id.activity_match_name_player_1);
        text_view_sets_player_1 = findViewById(R.id.activity_match_sets_player_1);
        text_view_games_player_1 = findViewById(R.id.activity_match_games_player_1);
        text_view_points_player_1 = findViewById(R.id.activity_match_points_player_1);

        text_view_player_2 = (TextView) findViewById(R.id.activity_match_name_player_2);
        text_view_sets_player_2 = findViewById(R.id.activity_match_sets_player_2);
        text_view_games_player_2 = findViewById(R.id.activity_match_games_player_2);
        text_view_points_player_2 = findViewById(R.id.activity_match_points_player_2);

        Intent i = getIntent();

        match = new Match();
        match.setId(i.getIntExtra("id_match",0));

        //Match match = (Match) i.getParcelableExtra("match_chosen");
        refresh_data();
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
            match = tmp.get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void update_UI(){
        if (match != null) {
            text_view_time.setText(Integer.toString(match.getMatch_time()));

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

        }
    }
}
