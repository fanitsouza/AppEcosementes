package edu.ifam.br.ecosemente.recycler;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.ifam.br.ecosemente.DetalheCompradorActivity;
import edu.ifam.br.ecosemente.R;
import edu.ifam.br.ecosemente.entity.Comprador;

public class CompradorAdapter extends RecyclerView.Adapter<CompradorViewHolder> {

    private List<Comprador> compradores;
    private Context context;

    public CompradorAdapter(List<Comprador> compradores, Context context){
        this.compradores = compradores;
        this.context = context;
    }
    @NonNull
    @Override
    public CompradorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comprador, parent, false);
        return new CompradorViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CompradorViewHolder holder, int position) {
        Comprador comprador = compradores.get(position);
        holder.tv_nomeComprador.setText(holder.tv_nomeComprador.getText().toString() + " " + comprador.getNome());
        holder.tv_cpfCnpjComprador.setText(holder.tv_cpfCnpjComprador.getText().toString() + " " + comprador.getCpfCnpj());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetalheCompradorActivity.class);
            intent.putExtra("id", comprador.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return  compradores.size();
    }

}
