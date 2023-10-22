package com.example.taskapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //Referencias
    EditText edtUser, edtPass;
    Button btnRegistro, btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistro = findViewById(R.id.registrar);

      btnRegistro.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent registro = new Intent(MainActivity.this, Registro.class);
              startActivity(registro);
          }
      });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = edtUser.getText().toString();
                String contrasenia = edtPass.getText().toString();

                if (iniciarSesion(usuario, contrasenia)) {
                    String nombreUsuario = obtenerNombreUsuario(usuario); // Obtiene el nombre de usuario

                    Intent intent = new Intent(MainActivity.this, TareaView.class);
                    intent.putExtra("nombre_usuario", nombreUsuario); // Envía el nombre de usuario a la actividad TareaView
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Inicio de sesión fallido.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean iniciarSesion(String usuario, String contrasenia) {

        File file = new File(getFilesDir(), "usuarios.csv");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length >= 4 && usuario.equals(userData[1]) && contrasenia.equals(userData[3])) {
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String obtenerNombreUsuario(String usuario) {
        File file = new File(getFilesDir(), "usuarios.csv");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length >= 4 && usuario.equals(userData[1])) {
                    return userData[0];
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}