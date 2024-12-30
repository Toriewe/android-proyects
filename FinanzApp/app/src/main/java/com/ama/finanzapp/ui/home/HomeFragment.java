package com.ama.finanzapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ama.finanzapp.CardAdapter;
import com.ama.finanzapp.Finanza;
import com.ama.finanzapp.R;
import com.ama.finanzapp.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ListView finanzaV;
    private List<Finanza> montos = new ArrayList<>();
    CardAdapter cardAdapter;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        finanzaV = (ListView) root.findViewById(R.id.lstFinanza);
        cardAdapter = new CardAdapter(getContext(), montos);
        obtenerFinanzas();

        finanzaV.setAdapter(cardAdapter);

        return root;
    }
    public void obtenerFinanzas(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String correoUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        db.collection("Finanzas")
                .whereEqualTo("email", correoUser)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        montos.clear();

                        for (QueryDocumentSnapshot document : task.getResult()){
                            Finanza finanza = document.toObject(Finanza.class);
                            montos.add(finanza);
                        }

                        if (cardAdapter != null){
                            cardAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}