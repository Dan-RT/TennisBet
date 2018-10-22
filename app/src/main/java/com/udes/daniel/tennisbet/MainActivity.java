package com.udes.daniel.tennisbet;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private MainActivity current_activity;
    private TextView test_TextView;
    private ListView listView_match;

    private boolean dataChanged = false;
    private ArrayAdapter adapter;
    private ArrayList<Match> ListMatchs = new ArrayList<Match>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        current_activity = this;

        Log.i("CIO", "MainActivity : onCreate !");

        test_TextView = (TextView) findViewById(R.id.activity_main_test_Tewt_View);
        listView_match = (ListView) findViewById(R.id.activity_main_list_match);

        retrieve_data();

        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, ListMatchs);
        listView_match.setAdapter(adapter);

        listView_match.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                launch_match(position);
            }
        });

        final SwipeRefreshLayout swipeRefresh = findViewById(R.id.swiperefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh_data();
                swipeRefresh.setRefreshing(false);
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
            tmp = request.execute("http://10.0.2.2:3000/parties").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ListMatchs.clear();
        //ListMatchs = tmp;
        for (Match match: tmp) {
            ListMatchs.add(match);
        }

    }

    private void update_UI(){
        if (ListMatchs != null) {
            current_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    adapter.notifyDataSetChanged();
                }
            });
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
