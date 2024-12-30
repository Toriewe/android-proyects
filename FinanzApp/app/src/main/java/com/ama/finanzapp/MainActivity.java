package com.ama.finanzapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    EditText correo, contrasena;
    Button ingresar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo = (EditText) findViewById(R.id.edtEmail);
        contrasena = (EditText) findViewById(R.id.edtContrasena);
        ingresar = (Button) findViewById(R.id.btnIngresar);

        inicializarFirestore();

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vCorreo, vContra;
                vCorreo = correo.getText().toString().trim();
                vContra = contrasena.getText().toString().trim();

                if (TextUtils.isEmpty(vCorreo) || TextUtils.isEmpty(vContra)){
                    correo.setError("Requerido");
                    contrasena.setError("Requerido");
                }else {
                    mAuth.signInWithEmailAndPassword(vCorreo, vContra)
                            .addOnCompleteListener(MainActivity.this, task -> {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(MainActivity.this, Inicio.class);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity.this, "Correo o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    public void inicializarFirestore(){
        FirebaseApp.initializeApp(this);
        FirebaseFirestore.getInstance();
    }

    public void onTextClick(View view){
        Intent intent = new Intent(MainActivity.this, RegistrarseActivity.class);
        startActivity(intent);
    }
}