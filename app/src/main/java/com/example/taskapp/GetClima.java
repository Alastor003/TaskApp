package com.example.taskapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

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

            //response = respuesta.sys.country;
            /*
            JSONObject weather = new JSONObject(response);
            JSONObject mainObject = weather.getJSONObject("main");
            double temperature = mainObject.getDouble("temp") - (273.15);
            int temperatureInteger = (int) Math.round(temperature);*/

            //JSONArray main = (JSONArray) weather.get("main");
            //JSONObject weather0 = (JSONObject) main.get(0);
            //String nameMain0 = (String) weather0.get("main");

            //response = String.valueOf(temperatureInteger) + '°';

        } catch (IOException e) //| JSONException
        {
            throw new RuntimeException(e);

        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("Probando url", s);

        try {
            // Parsea la respuesta JSON
            JSONObject jsonResponse = new JSONObject(s);

            // Obtiene el valor de 'main>temp'
            JSONObject mainObject = jsonResponse.getJSONObject("main");
            double temperaturaKelvin = mainObject.getDouble("temp");

            // Convierte de Kelvin a Celsius (o Fahrenheit, según sea necesario)
            //double temperaturaCelsius = temperaturaKelvin - 273.15;
            int tempCelcius = (int) Math.round(temperaturaKelvin - 273.15);

            // Obtiene el valor de 'sys>country'
            JSONObject sysObject = jsonResponse.getJSONObject("sys");
            String pais = sysObject.getString("country");

            // Ahora tienes 'temperaturaCelsius' y 'pais' disponibles

            // Si deseas mostrar estos valores en un TextView o EditText:
            String resultado = "Temperatura: " + tempCelcius + "°C, País: " + pais;

            // Puedes mostrar el resultado en un TextView
            // textViewResultado.setText(resultado);
            //EditText e = null;
            //e.findViewById(R.id.edtClima);
            //e.setText(resultado);


            // O en un EditText
            // editTextResultado.setText(resultado);

            // Puedes mostrar el resultado en el Log para verificar
            Log.i("Resultado API Clima", resultado);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

