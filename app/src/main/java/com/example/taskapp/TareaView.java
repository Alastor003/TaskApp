package com.example.taskapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class TareaView extends AppCompatActivity {
    private TextView userLogin, userDate;
    private RecyclerView recyclerView;
    private TareaAdapter tareaAdapter;
    private List<Tarea> listaDeTareas = new ArrayList<>();
    private static final int CREAR_TAREA_REQUEST = 1;
    ImageButton btnBack;
    Button btnNuevaTarea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea);

        String nombreUsuario = getIntent().getStringExtra("nombre_usuario");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = dateFormat.format(new Date());


        recyclerView = findViewById(R.id.listaTareas);
        btnBack = findViewById(R.id.btnBack);
        btnNuevaTarea = findViewById(R.id.btnNuevaTarea);
        userLogin = findViewById(R.id.userLogin);
        userDate = findViewById(R.id.userDate);
        tareaAdapter = new TareaAdapter(listaDeTareas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tareaAdapter);

        if (nombreUsuario != null) {
            Toast.makeText(this, "Bienvenido, " + nombreUsuario, Toast.LENGTH_SHORT).show();
        }

        userLogin.setText("Usuario: " + nombreUsuario);
        userDate.setText("Fecha: " + fechaActual);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnNuevaTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TareaView.this, CrearTarea.class);
                startActivityForResult(intent, CREAR_TAREA_REQUEST);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREAR_TAREA_REQUEST && resultCode == RESULT_OK) {

            Tarea nuevaTarea = data.getParcelableExtra("nueva_tarea");

            listaDeTareas.add(nuevaTarea);

            tareaAdapter.notifyDataSetChanged();
        }
    }
}