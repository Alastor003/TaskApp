package com.example.taskapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class ImageDownloader extends AsyncTask<String, Integer, Bitmap> {

    private Bitmap imagenDescargada = null;
    private  ImageView imageView;

    public ImageDownloader(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String Url_consulta = strings[0];
        try {
            URL consulta = new URL(Url_consulta);
            InputStream contenido = (InputStream) consulta.getContent();
            this.imagenDescargada = BitmapFactory.decodeStream(contenido);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return imagenDescargada;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (imagenDescargada == null) return;
        this.imageView.setImageBitmap(bitmap);

    }
}
