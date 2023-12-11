package com.example.taskapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    private static final String TAG = "RegistroActivity";

    EditText edtNombre, edtUsuario, edtEmail, edtContrasenia;
    ImageButton btnVolver;
    Button btnRegistrar;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar Firebase Auth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edtNombre = findViewById(R.id.edtNombre);
        edtUsuario = findViewById(R.id.edtUsuario);
        edtEmail = findViewById(R.id.edtEmail);
        edtContrasenia = findViewById(R.id.edtContrasenia);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolver = findViewById(R.id.btnBack);

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = edtNombre.getText().toString();
                String usuario = edtUsuario.getText().toString();
                String email = edtEmail.getText().toString();
                String contrasenia = edtContrasenia.getText().toString();

                if (nombre.isEmpty() || usuario.isEmpty() || email.isEmpty() || contrasenia.isEmpty()) {
                    Toast.makeText(Registro.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    registrarUsuario(email, contrasenia, nombre, usuario);
                }
            }
        });
    }

    private void registrarUsuario(String email, String contrasenia, final String nombre, final String usuario) {
        mAuth.createUserWithEmailAndPassword(email, contrasenia)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            guardarDatosUsuarioFirestore(user.getUid(), nombre, usuario, email);

                            // Registro exitoso, dirigir al usuario al MainActivity
                            Intent intent = new Intent(Registro.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Registro.this, "Error al registrarse.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void guardarDatosUsuarioFirestore(String userId, String nombre, String usuario, String email) {
        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("nombre", nombre);
        usuarioMap.put("usuario", usuario);
        usuarioMap.put("email", email);

        // Agregar datos del usuario a la colección "usuarios" en Firestore
        db.collection("usuarios")
                .document(userId)
                .set(usuarioMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Registro.this, "Se registro usuario con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Error al guardar datos en Firestore", task.getException());
                            Toast.makeText(Registro.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
