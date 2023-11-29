package com.example.taskapp;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class CrearTarea extends AppCompatActivity {

    EditText edtTitulo, edtDescripcion, edtHora;
    Button btnGuardar, btnVolverMenu;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);

        db = FirebaseFirestore.getInstance();

        edtTitulo = findViewById(R.id.edtTitulo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtHora = findViewById(R.id.edtHora);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVolverMenu = findViewById(R.id.btnVolverMenu);

        btnVolverMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CrearTarea.this, TareaView.class);
                startActivity(intent);
                finish();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titulo = edtTitulo.getText().toString();
                String descripcion = edtDescripcion.getText().toString();
                String hora = edtHora.getText().toString();

                guardarTareaEnFirestore(titulo, descripcion, hora);
                Intent intent = new Intent(CrearTarea.this, TareaView.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void guardarTareaEnFirestore(String titulo, String descripcion, String hora) {
        Map<String, Object> tarea = new HashMap<>();
        tarea.put("titulo", titulo);
        tarea.put("descripcion", descripcion);
        tarea.put("hora", hora);
        tarea.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid()); // ID del usuario actual
        tarea.put("status", "En espera");

        db.collection("tareas")
                .add(tarea)
                .addOnSuccessListener(documentReference -> {
                    mostrarMensaje("Tarea creada con éxito");
                })
                .addOnFailureListener(e -> {
                    mostrarMensaje("Error al crear la tarea. Por favor, inténtalo de nuevo.");
                });
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}