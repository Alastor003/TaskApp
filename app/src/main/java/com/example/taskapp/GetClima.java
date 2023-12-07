package com.example.taskapp;

import android.icu.lang.UCharacterEnums;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetClima extends AsyncTask<String, Integer, String> {
    OkHttpClient client = new OkHttpClient();
    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String response = "";

        try {
            response = run(url);
            JSONObject weather = new JSONObject(response);
            //JSONArray main = (JSONArray) weather.get("main");
            JSONObject mainObject = weather.getJSONObject("main");
            //JSONObject weather0 = (JSONObject) main.get(0);
            double temperature = mainObject.getDouble("temp") - (273.15);
            int temperatureInteger = (int) Math.round(temperature);
            //String nameMain0 = (String) weather0.get("main");

            response = String.valueOf(temperatureInteger) + '°';

        }
        catch (IOException | JSONException e)
        {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Probando url", s);
    }
}
