package com.ama.finanzapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrarseActivity extends AppCompatActivity {
    EditText newNombre, newCorreo, newContra;
    Button agregarUsu;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        newNombre = (EditText) findViewById(R.id.edtNombre);
        newCorreo = (EditText) findViewById(R.id.edtCrearCorreo);
        newContra = (EditText) findViewById(R.id.edtCrearContra);
        agregarUsu = (Button) findViewById(R.id.btnCrearUsu);

        agregarUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();
                }

                String nombre, correo, contra;
                nombre = newNombre.getText().toString().trim();
                correo = newCorreo.getText().toString().trim();
                contra = newContra.getText().toString().trim();

                if (nombre.isEmpty() && correo.isEmpty() && contra.isEmpty()){
                    newNombre.setError("Requerido");
                    newCorreo.setError("Requerido");
                    newContra.setError("Requerido");
                }else {
                    mAuth.createUserWithEmailAndPassword(correo, contra)
                            .addOnCompleteListener(RegistrarseActivity.this, task -> {
                        if (task.isSuccessful()){
                            guardarUserFirestore(correo);

                            Intent intent = new Intent(RegistrarseActivity.this, MainActivity.class);
                            startActivity(intent);

                        }else {
                            Toast.makeText(RegistrarseActivity.this, "Hubo un problema al registrarse: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            public void guardarUserFirestore(String correo){
                String nNombre;

                nNombre = newNombre.getText().toString().trim();

                Usuario usuario = new Usuario();
                usuario.setEmail(correo);
                usuario.setNombre(nNombre);

                db.collection("Usuarios")
                        .document(correo)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                if (task.getResult().exists()){
                                    Toast.makeText(RegistrarseActivity.this, "El correo ya ha sido registrado.", Toast.LENGTH_SHORT).show();
                                } else {
                                    db.collection("Usuarios")
                                            .document(correo)
                                            .set(usuario)
                                            .addOnCompleteListener(documentReference -> {
                                                Toast.makeText(RegistrarseActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            } else {
                                Toast.makeText(RegistrarseActivity.this, "Hubo un error de verificacion: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}