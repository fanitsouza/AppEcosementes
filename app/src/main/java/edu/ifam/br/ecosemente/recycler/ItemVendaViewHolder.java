package edu.ifam.br.ecosemente.recycler;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.ifam.br.ecosemente.R;

public class ItemVendaViewHolder extends RecyclerView.ViewHolder {

     Spinner spinner;
     EditText etQuantidade;
     TextView tvPreco;

    public ItemVendaViewHolder(@NonNull View itemView) {
        super(itemView);
        spinner = itemView.findViewById(R.id.spinnerItemVenda);
        etQuantidade = itemView.findViewById(R.id.editQuantidade);
        tvPreco = itemView.findViewById(R.id.tvPrecoItemVenda);
    }
}
