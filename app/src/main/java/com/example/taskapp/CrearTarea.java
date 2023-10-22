package com.example.taskapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CrearTarea extends AppCompatActivity {

    EditText edtTitulo, edtDescripcion, edtHora;
    Button btnGuardar, btnVolverMenu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);

        edtTitulo = findViewById(R.id.edtTitulo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtHora = findViewById(R.id.edtHora);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVolverMenu = findViewById(R.id.btnVolverMenu);

        btnVolverMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titulo = edtTitulo.getText().toString();
                String descripcion = edtDescripcion.getText().toString();
                String hora = edtHora.getText().toString();

                hora = hora.trim() + " HS";

                Tarea nuevaTarea = new Tarea(titulo, descripcion, hora);

                Intent intent = new Intent();
                intent.putExtra("nueva_tarea", (Parcelable) nuevaTarea);
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }
}