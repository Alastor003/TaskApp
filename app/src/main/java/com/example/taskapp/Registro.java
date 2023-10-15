package com.example.taskapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Registro extends AppCompatActivity {
    EditText edtNombre, edtUsuario, edtEmail, edtContrasenia;
    Button btnRegistrar;
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edtNombre = findViewById(R.id.edtNombre);
        edtUsuario = findViewById(R.id.edtUsuario);
        edtEmail = findViewById(R.id.edtEmail);
        edtContrasenia = findViewById(R.id.edtContrasenia);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnBack = findViewById(R.id.btnBack);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                if (usuarioExiste(usuario, email)) {
                    Toast.makeText(Registro.this, "Usuario o correo electrónico ya existen", Toast.LENGTH_SHORT).show();
                } else {
                    guardarDatosUsuario(nombre, usuario, email, contrasenia);

                    Intent intent = new Intent(Registro.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(Registro.this, "Registro completo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void guardarDatosUsuario(String nombre, String usuario, String email, String contrasenia) {
        File file = new File(getFilesDir(), "usuarios.csv");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true)); // true para agregar al archivo existente

            String userData = nombre + "," + usuario + "," + email + "," + contrasenia;
            writer.write(userData);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean usuarioExiste(String usuario, String email) {
        File file = new File(getFilesDir(), "usuarios.csv");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length >= 3 && (usuario.equals(userData[1]) || email.equals(userData[2]))) {
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}