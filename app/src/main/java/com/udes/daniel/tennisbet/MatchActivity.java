package com.udes.daniel.tennisbet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MatchActivity extends AppCompatActivity {

    private MatchActivity current_activity;
    private Match match;
    private TextView text_view_player_1;
    private TextView text_view_player_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        current_activity = this;

        text_view_player_1 = (TextView) findViewById(R.id.activity_match_name_player_1);
        text_view_player_2 = (TextView) findViewById(R.id.activity_match_name_player_2);

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
            text_view_player_1.setText(match.getPlayer_1().getFirst_name() + " " + match.getPlayer_1().getSurname());
            text_view_player_2.setText(match.getPlayer_2().getFirst_name() + " " + match.getPlayer_2().getSurname());
        }
    }
}
