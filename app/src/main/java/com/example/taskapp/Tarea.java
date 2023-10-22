package com.example.taskapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Tarea implements Parcelable {
    private String titulo;
    private String descripcion;
    private String hora;
    private String status;


    public Tarea(String titulo, String descripcion, String hora) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.hora = hora;
        this.status = "En espera";
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    protected Tarea(Parcel in) {
        titulo = in.readString();
        descripcion = in.readString();
        hora = in.readString();
        status = in.readString();
    }

    public static final Creator<Tarea> CREATOR = new Creator<Tarea>() {
        @Override
        public Tarea createFromParcel(Parcel in) {
            return new Tarea(in);
        }

        @Override
        public Tarea[] newArray(int size) {
            return new Tarea[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeString(hora);
        dest.writeString(status);
    }
}