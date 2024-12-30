package com.ama.finanzapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class ActualizarActivity extends AppCompatActivity {
    TextView dato;
    EditText verG,verP, verM;
    CheckBox actualizar;
    Button volver, eliminar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        verG = findViewById(R.id.edtVerG);
        verP = findViewById(R.id.edtVerP);
        verM = findViewById(R.id.edtVerM);
        dato = findViewById(R.id.txvDato);
        actualizar = findViewById(R.id.chkActualizar);
        volver = findViewById(R.id.btnRegresar);
        eliminar = findViewById(R.id.btnEliminar);

        Intent intent = getIntent();
        if (intent != null){
            String idDoc = intent.getStringExtra("idDoc");

            if (idDoc != null) {
                DocumentReference finanzasRef = db.collection("Finanzas").document(idDoc);

                finanzasRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                String ventaV, compraV, montoV;

                                compraV = String.valueOf(document.getDouble("compra"));
                                ventaV = String.valueOf(document.getDouble("venta"));
                                montoV = String.valueOf(document.getDouble("monto"));

                                verG.setText(ventaV);
                                verP.setText(compraV);
                                verM.setText(montoV);
                            } else {
                                Toast.makeText(ActualizarActivity.this, "Finanza no encontrada", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(ActualizarActivity.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        actualizar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (actualizar.isChecked()){
                    volver.setText("Actualizar");

                    dato.setText("(El nuevo monto aparecerá al actualizar)");

                    verG.setFocusable(true);
                    verP.setFocusable(true);

                    verG.setEnabled(true);
                    verP.setEnabled(true);

                    verG.setFocusableInTouchMode(true);
                    verP.setFocusableInTouchMode(true);

                }else {
                    volver.setText("Volver");

                    dato.setText("");

                    verG.setFocusable(false);
                    verP.setFocusable(false);
                    verM.setFocusable(false);

                    verG.setEnabled(false);
                    verP.setEnabled(false);

                    verG.setFocusableInTouchMode(false);
                    verP.setFocusableInTouchMode(false);
                }
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actualizar.isChecked()){
                    if (intent != null){
                        String idDoc = intent.getStringExtra("idDoc");

                        if (idDoc != null){
                            String actuGP;
                            Double actuG, actuP, actuM, actuMN;

                            actuG = Double.parseDouble(verG.getText().toString());
                            actuP = Double.parseDouble(verP.getText().toString());

                            actuM = actuG - actuP;

                            DecimalFormat formato = new DecimalFormat("#.##");

                            String actuF = formato.format(actuM);

                            actuMN = Double.parseDouble(actuF);

                            if (actuM < 0){
                                actuGP = "Pérdida";
                            } else {
                                actuGP = "Ganancia";
                            }

                            DocumentReference finanzaRef = db.collection("Finanzas").document(idDoc);

                            finanzaRef
                                    .update(
                                            "venta", actuG,
                                            "compra", actuP,
                                            "monto", actuMN,
                                            "ganoPer", actuGP)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intentV = new Intent(ActualizarActivity.this, Inicio.class);
                                            startActivity(intentV);

                                            Toast.makeText(ActualizarActivity.this, "Finanza actualizada", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ActualizarActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else {
                            Toast.makeText(ActualizarActivity.this, "Finanza no encontrada", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(ActualizarActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Intent intent = new Intent(ActualizarActivity.this, Inicio.class);
                    startActivity(intent);
                }
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intent != null){
                    String idDoc = intent.getStringExtra("idDoc");

                    if (idDoc != null){
                        db.collection("Finanzas").document(idDoc)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ActualizarActivity.this, "Finanza borrada", Toast.LENGTH_SHORT).show();

                                        Intent intentE = new Intent(ActualizarActivity.this, Inicio.class);
                                        startActivity(intentE);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ActualizarActivity.this, "Error al borrar", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
        });
    }
}