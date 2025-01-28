package edu.ifam.br.ecosemente.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.ifam.br.ecosemente.R;

public class VendaViewHolder extends RecyclerView.ViewHolder {
     TextView tv_nomeComprador;
     TextView tv_dataVenda;
     TextView tv_valorVenda;

    public VendaViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_nomeComprador = itemView.findViewById(R.id.tv_compradorVenda);
        tv_dataVenda = itemView.findViewById(R.id.tv_dataVenda);
        tv_valorVenda = itemView.findViewById(R.id.tv_valorVenda);
    }
}
