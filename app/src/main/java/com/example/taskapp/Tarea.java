package com.example.taskapp;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Tarea {
    private String  status;
    private String descripcion;
    private String hora;
    private String titulo;

    public Tarea() {
        // Constructor vacío necesario para Firestore
    }

    public Tarea(String titulo, String descripcion, String hora, String status) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.hora = hora;
        this.status = status;
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

    public String getTitulo() {
        return titulo;
    }

    public String getStatus() {
        return status;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
