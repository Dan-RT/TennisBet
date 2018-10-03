package com.udes.daniel.tennisbet;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private MainActivity current_activity;
    private TextView test_TextView;
    private Button test_Button;
    private Button reset_Button;
    private Button launch_match_activity_Button;
    private boolean toggle = false;

    private ArrayList<Match> ListMatchs = new ArrayList<Match>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        current_activity = this;

        Log.i("CIO", "MainActivity : onCreate !");

        test_TextView = (TextView) findViewById(R.id.activity_main_test_Tewt_View);
        test_Button = (Button) findViewById(R.id.activity_main_test_button);
        reset_Button = (Button) findViewById(R.id.activity_main_reset_button);
        launch_match_activity_Button = (Button) findViewById(R.id.activity_main_launch_match_activity);

        refresh_data();

        test_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toggle) {
                    test_TextView.setText("TEST");
                    toggle = true;
                } else {
                    test_TextView.setText("RE-TEST");
                    toggle = false;
                }
            }
        });

        reset_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*test_TextView.setText("Hello World!");
                refresh_data();
                toggle = false;*/
                launch_match(1);
            }
        });

        launch_match_activity_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_match(2);
            }
        });

    }

    private void refresh_data () {
        retrieve_data();
        update_UI();
    }

    private void retrieve_data () {
        AsyncServerRequest request = new AsyncServerRequest(current_activity);

        ArrayList<Match> tmp =  new ArrayList<Match>();
        try {
            tmp = request.execute("http://10.0.2.2:3000/ListMatchs").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ListMatchs.clear();
        ListMatchs = tmp;

    }

    private void update_UI(){
        if (ListMatchs != null) {
            //create the UI
        }
    }

    private void launch_match(int id) {
        Intent match_activity = new Intent(current_activity, MatchActivity.class);
        if (ListMatchs != null) {
            //match_activity.putExtra("match_chosen", (Parcelable) ListMatchs.get(0));

            for (Match match_tmp:ListMatchs) {
                if (match_tmp.getId() == id) {
                    match_activity.putExtra("id_match", (Integer) match_tmp.getId());
                }
            }

        } else {
            match_activity.putExtra("message", "data passed through intent");
        }
        startActivity(match_activity);
    }

    public void add_match_to_ListMatchs(Match match) {
        this.ListMatchs.add(match);
    }

    @Override
    protected void onRestart() { super.onRestart(); Log.i("CIO", "MainActivity : onRestart !"); }

    @Override
    protected void onStart() { super.onStart(); Log.i("CIO", "MainActivity : onStart !"); }

    @Override
    protected void onResume() { super.onResume(); Log.i("CIO", "MainActivity : onResume !"); }

    @Override
    protected void onPause() { super.onPause(); Log.i("CIO", "MainActivity : onPause !"); }

    @Override
    protected void onStop() { super.onStop(); Log.i("CIO", "MainActivity : onStop !"); }

    @Override
    protected void onDestroy() { super.onDestroy(); Log.i("CIO", "MainActivity : onDestroy !"); }

}
