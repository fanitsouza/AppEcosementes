package edu.ifam.br.ecosemente.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.ifam.br.ecosemente.R;

public class CompradorViewHolder extends RecyclerView.ViewHolder {

    TextView tv_nomeComprador;
    TextView tv_cpfCnpjComprador;

    public CompradorViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_nomeComprador = itemView.findViewById(R.id.tv_comprador_nome);
        tv_cpfCnpjComprador = itemView.findViewById(R.id.tv_comprador_cpfcnpj);

    }
}
