package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        tareaAdapter = new TareaAdapter(options);
        recyclerView.setAdapter(tareaAdapter);

        if (nombreUsuario != null) {
            Toast.makeText(this, "Bienvenido, " + nombreUsuario, Toast.LENGTH_SHORT).show();
        }

        userLogin = findViewById(R.id.userLogin);
        userDate = findViewById(R.id.userDate);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userEmail = user.getEmail();
            db.collection("usuarios")
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            String nombre = task.getResult().getDocuments().get(0).getString("nombre");
                            if (nombre != null) {
                                userLogin.setText("Usuario: " + nombre);
                            }
                        } else {
                            Toast.makeText(this, "No se encontró el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = dateFormat.format(new Date());
        userDate.setText("Fecha: " + fechaActual);


        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnNuevaTarea = findViewById(R.id.btnNuevaTarea);

        btnBack.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();

            Toast.makeText(this, "Sesión cerrada con éxito", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(TareaView.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnNuevaTarea.setOnClickListener(view -> {
            Intent intent = new Intent(TareaView.this, CrearTarea.class);
            startActivityForResult(intent, CREAR_TAREA_REQUEST);
        });

        GetClima getClima = new GetClima((TextView) findViewById(R.id.edtClima));
        //Api con Lat y Lon ArgentinaBuenosAires.
        getClima.execute("https://api.openweathermap.org/data/2.5/weather?lat=-34.6118&lon=-58.4173&appid=3842f0c5a972487dfec9c3306184e87b"); //Link de la api.
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

    private void eliminarTarea(int position) {
        tareaAdapter.eliminarTarea(position);
        Toast.makeText(this, "Tarea eliminada con éxito", Toast.LENGTH_SHORT).show();
    }
}
