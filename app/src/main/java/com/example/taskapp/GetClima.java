package com.example.taskapp;

import android.os.AsyncTask;
import android.util.Log;

public class GetClima extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        return url;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Probando url", s);
    }
}
