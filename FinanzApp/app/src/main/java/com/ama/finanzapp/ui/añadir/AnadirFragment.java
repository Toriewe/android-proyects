package com.ama.finanzapp.ui.añadir;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ama.finanzapp.Finanza;
import com.ama.finanzapp.Inicio;
import com.ama.finanzapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.UUID;

public class AnadirFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText ganancia, perdida;
    Button agregar;

    public AnadirFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AnadirFragment newInstance(String param1, String param2) {
        AnadirFragment fragment = new AnadirFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_anadir, container, false);

        ganancia = (EditText) view.findViewById(R.id.edtGanancia);
        perdida = (EditText) view.findViewById(R.id.edtPerdida);
        agregar = (Button) view.findViewById(R.id.btnAgregar);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    String aumento, descuento, correo;

                    correo = user.getEmail();
                    aumento = ganancia.getText().toString();
                    descuento = perdida.getText().toString();

                    if (aumento.isEmpty() && descuento.isEmpty()){
                        ganancia.setError("Ingrese un valor");
                        perdida.setError("Ingrese un valor");
                    }else {
                        try {
                            Double monto, montoN, aumentoA, descuentoA;
                            String goPA;

                            aumentoA = Double.parseDouble(aumento);
                            descuentoA = Double.parseDouble(descuento);

                            monto = aumentoA - descuentoA;

                            DecimalFormat formato = new DecimalFormat("#.##");

                            String montoF = formato.format(monto);

                            montoN = Double.parseDouble(montoF);

                            if (monto <= 0){
                                goPA = "Pérdida";
                            }else {
                                goPA = "Ganancia";
                            }

                            String randomId = UUID.randomUUID().toString();

                            Finanza finanza = new Finanza();

                            finanza.setIdDoc(randomId);
                            finanza.setEmail(correo);
                            finanza.setVenta(aumentoA);
                            finanza.setCompra(descuentoA);
                            finanza.setMonto(montoN);
                            finanza.setGanoPer(goPA);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            db.collection("Finanzas")
                                    .document(randomId)
                                    .set(finanza)
                                    .addOnCompleteListener(task -> {
                                        Toast.makeText(getContext(), "Monto agregado", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(), Inicio.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getContext(), "Error al agregar, ponerse en contacto",
                                                Toast.LENGTH_SHORT).show();
                                    });

                        } catch (NumberFormatException e) {
                            ganancia.setError("Ingrese valores numéricos");
                            perdida.setError("Ingrese valores numéricos");
                            Toast.makeText(getContext(), "Ingrese valores numéricos por favor", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        return view;
    }
}
