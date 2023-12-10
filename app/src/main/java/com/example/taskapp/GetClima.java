package com.example.taskapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class Coord {
    public double lon;
    public double lat;
}

class Sys {
    public String country;
}

class Main {
    public double temp;
    public double feels_like;
}

class Data {
    public Coord coord;
    public Sys sys;
    public Main main;
}

public class GetClima extends AsyncTask<String, Integer, String> {
    OkHttpClient client = new OkHttpClient();
    private TextView textView; // Nueva variable para el TextView

    // Constructor que acepta un TextView
    public GetClima(TextView textView) {
        this.textView = textView;
    }

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
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Data respuesta = mapper.readValue(response, Data.class);
            double latitud = respuesta.coord.lat;
            double longitud = respuesta.coord.lon;
            String pais = respuesta.sys.country;
            double temperaturaKelvin = respuesta.main.temp;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Probando url", s);

        try {
            JSONObject jsonResponse = new JSONObject(s);
            JSONObject mainObject = jsonResponse.getJSONObject("main");
            double temperaturaKelvin = mainObject.getDouble("temp");

            int tempCelcius = (int) Math.round(temperaturaKelvin - 273.15);
            JSONObject sysObject = jsonResponse.getJSONObject("sys");
            String pais = sysObject.getString("country");
            String resultado = "Temperatura actual en " + pais + " es de " + tempCelcius + "°C.";

            // Actualizar el TextView con el resultado
            if (textView != null) {
                textView.setText(resultado);
            }

            Log.i("Resultado API Clima", resultado);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
