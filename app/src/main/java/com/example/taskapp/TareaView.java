package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TareaView extends AppCompatActivity {
    private TextView userLogin, userDate;
    private RecyclerView recyclerView;
    private TareaAdapter tareaAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private static final int CREAR_TAREA_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        String nombreUsuario = getIntent().getStringExtra("nombre_usuario");

        recyclerView = findViewById(R.id.listaTareas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = db.collection("tareas")
                .whereEqualTo("userId", mAuth.getCurrentUser().getUid());

        FirestoreRecyclerOptions<Tarea> options = new FirestoreRecyclerOptions.Builder<Tarea>()
                .setQuery(query, Tarea.class)
                .build();

        tareaAdapter = new TareaAdapter(options);
        recyclerView.setAdapter(tareaAdapter);

        if (nombreUsuario != null) {
            Toast.makeText(this, "Bienvenido, " + nombreUsuario, Toast.LENGTH_SHORT).show();
        }

        userLogin = findViewById(R.id.userLogin);
        userDate = findViewById(R.id.userDate);

        // Mostrar el nombre de usuario obtenido desde el intent
        userLogin.setText("Usuario: " + nombreUsuario);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = dateFormat.format(new Date());
        userDate.setText("Fecha: " + fechaActual);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnNuevaTarea = findViewById(R.id.btnNuevaTarea);

        btnBack.setOnClickListener(view -> finish());

        btnNuevaTarea.setOnClickListener(view -> {
            Intent intent = new Intent(TareaView.this, CrearTarea.class);
            startActivityForResult(intent, CREAR_TAREA_REQUEST);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        tareaAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        tareaAdapter.stopListening();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREAR_TAREA_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(this, "Nueva tarea creada con éxito", Toast.LENGTH_SHORT).show();
        }
    }
}
