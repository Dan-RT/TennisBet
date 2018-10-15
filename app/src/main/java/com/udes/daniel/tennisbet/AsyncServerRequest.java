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

        return CustomApplication.createListMatchFromJSon(obj);
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



}
