package com.ama.finanzapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CardAdapter extends ArrayAdapter<Finanza> {
    public CardAdapter(Context context, List<Finanza> data) {
        super(context, R.layout.cardview_item, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cardView = convertView;

        if (cardView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            cardView = inflater.inflate(R.layout.cardview_item, parent, false);
        }

        //Obtener el objeto Finanza
        Finanza finanza = getItem(position);

        TextView gop = cardView.findViewById(R.id.txvGoP);
        TextView monto = cardView.findViewById(R.id.txvMonto);

        if (finanza == null){
            gop.setText("...");
            monto.setText("No hay datos disponibles");
        }else {
            gop.setText(finanza.getGanoPer());
            monto.setText(Double.toString(finanza.getMonto()));
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActualizarActivity.class);
                intent.putExtra("idDoc", finanza.getIdDoc());

                getContext().startActivity(intent);
            }
        });

        return cardView;
    }
}
