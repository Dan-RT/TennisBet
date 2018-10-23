package com.udes.daniel.tennisbet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;

public class MatchActivity extends AppCompatActivity{

    final Context context = this;
    private MatchActivity current_activity;
    private Match match;
    private TextView text_view_player_1;
    private TextView text_view_player_2;
    private RadioButton radio_button_bet_player_1;
    private RadioButton radio_button_bet_player_2;
    private RadioGroup radio_group_players;
    private Button match_bet_button;
    private EditText result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        current_activity = this;

        text_view_player_1 = (TextView) findViewById(R.id.activity_match_name_player_1);
        text_view_player_2 = (TextView) findViewById(R.id.activity_match_name_player_2);




        /*radio_button_bet_player_1.setText(text_view_player_1.getText());
        radio_button_bet_player_2.setText(text_view_player_2.getText());*/

        match_bet_button = (Button) findViewById(R.id.activity_match_bet_button);

        match_bet_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.betting_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.bet_value);
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
                                        String player = "";
                                        LayoutInflater l = LayoutInflater.from(context);
                                        View ptView = l.inflate(R.layout.betting_dialog, null);
                                        // get user input and set it to result
                                        // edit text
                                        int selectedId = radio_group_players.getCheckedRadioButtonId();
                                        RadioButton PlayerButton = (RadioButton) ptView.findViewById(selectedId);
                                        if(selectedId == radio_button_bet_player_1.getId())
                                            player = radio_button_bet_player_1.getText().toString();
                                        else if(selectedId == radio_button_bet_player_2.getId())
                                            player = radio_button_bet_player_2.getText().toString();
                                        match.AddTo_bet_amount(Double.parseDouble(userInput.getText().toString()));
                                        ((TextView) findViewById(R.id.display_bet)).setText("You just bet " + Double.parseDouble(userInput.getText().toString()) + "$ for " + player
                                        + "there is now: " + match.getBet_amount() + "$ in game.");
                                        match_bet_button.setEnabled(false);



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
        String URL = "http://10.0.2.2:3000/match/" + match.getId();

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
