package com.example.taskapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    // Referencias
    EditText edtUser, edtPass;
    Button btnRegistro, btnLogin;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkConnection();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistro = findViewById(R.id.registrar);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirRegistro();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });
    }

    private void abrirRegistro() {
        Intent registro = new Intent(MainActivity.this, Registro.class);
        startActivity(registro);
    }

    private void iniciarSesion() {
        String usuario = edtUser.getText().toString();
        String contrasenia = edtPass.getText().toString();

        if (!usuario.isEmpty() && !contrasenia.isEmpty()) {
            iniciarSesionFirebase(usuario, contrasenia);
        } else {
            mostrarMensaje("Ingresa usuario y contraseña.");
        }
    }

    private void iniciarSesionFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(Task task) {
                        if (task.isSuccessful()) {
                            obtenerNombreUsuario(email);
                        } else {
                            mostrarMensaje("Inicio de sesión fallido.");
                        }
                    }
                });
    }

    private void obtenerNombreUsuario(String email) {
        db.collection("usuarios")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().getDocuments().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String nombreUsuario = document.getString("nombre");
                            redirigirATareaView(nombreUsuario);
                        } else {
                            mostrarMensaje("Error al obtener información del usuario.");
                        }
                    }
                });
    }

    private void redirigirATareaView(String nombreUsuario) {
        Intent intent = new Intent(MainActivity.this, TareaView.class);
        intent.putExtra("nombre_usuario", nombreUsuario);
        startActivity(intent);
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
    }


    public void download(View v) {
        ImageView imageView = findViewById(R.id.imageView);
        if (imageView.getVisibility() == View.INVISIBLE) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
        ImageDownloader descarga = new ImageDownloader(imageView);
        descarga.execute("https://scontent.fepa2-1.fna.fbcdn.net/v/t39.30808-6/405251742_7673753482641159_1720167608337291579_n.jpg?_nc_cat=109&ccb=1-7&_nc_sid=3635dc&_nc_ohc=lAk3tujHHJsAX-buVqX&_nc_ht=scontent.fepa2-1.fna&cb_e2o_trans=t&oh=00_AfAVcX33uj07QQCdZ86SH9nAaQd8_Wcn6wFmCTFmvvaN6A&oe=656BB993");
    }

    public void checkConnection(View view) {
        checkConnection();
    }

    //Conexión y verificación de internet.
    public void checkConnection() {
        LinearLayout noInternetConnection = findViewById(R.id.noConnectionLayout);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Asynk Task
            GetClima getClima = new GetClima();
            //Api con Lat y Lon ArgentinaBuenosAires.
            getClima.execute("https://api.openweathermap.org/data/2.5/weather?lat=-34.6118&lon=-58.4173&appid=3842f0c5a972487dfec9c3306184e87b"); //Link de la api.
            noInternetConnection.setVisibility(View.INVISIBLE);

        } else {
            noInternetConnection.setVisibility(View.VISIBLE);
        }

    }
}
