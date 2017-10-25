package com.example.nikolaistakheiko.runtest1;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by nikolaistakheiko on 2017-10-24.
 */

public class fetchData extends AsyncTask<Void, Void, Void> {
    String data = "";
    String list;
    JSONArray JA;
    double lat;
    double lng;

    public fetchData() {
    }

    public fetchData(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        URL url = null;
        try {
            url = new URL("http://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lng + "&appid=76675786ff883ece6ad493272e2ace29");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            int dataInt = bufferedReader.read();
//            while(dataInt != -1) {
//                char current = (char) dataInt;
//
//                data += current;
//
//                dataInt = bufferedReader.read();
//            }
            String line = "";
            while(line != null) {
                line = bufferedReader.readLine();
                data += line;
            }
            JSONObject JO = new JSONObject(data);
            JA = JO.getJSONArray("list");
//            list = JA.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        MainActivity.weather.setText(this.list);
        MainActivity.weatherJSONArray = JA;
    }
}
