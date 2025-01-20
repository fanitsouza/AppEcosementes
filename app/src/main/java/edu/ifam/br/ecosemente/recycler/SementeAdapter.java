package edu.ifam.br.ecosemente.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.ifam.br.ecosemente.DetalheSemente;
import edu.ifam.br.ecosemente.R;
import edu.ifam.br.ecosemente.entity.Semente;

public class SementeAdapter extends RecyclerView.Adapter<SementeAdapter.SementeViewHolder> {
    private List<Semente> sementes;
    private Context context;

    public SementeAdapter(List<Semente> sementes, Context context) {
        this.sementes = sementes;
        this.context = context;
    }

    @NonNull
    @Override
    //Cria a ligação com layout que ira conter o Linear Layout com as informações do fornecedor
    public SementeAdapter.SementeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_semente, parent, false);
        return new SementeViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SementeAdapter.SementeViewHolder holder, int position) {
        Semente semente = sementes.get(position);
        holder.tv_nomeSemente.setText(holder.tv_nomeSemente.getText().toString() + " " + semente.getNome());
        holder.tv_quantidadeSemente.setText(holder.tv_quantidadeSemente.getText().toString() + " " + semente.getQuantidade());
        holder.tv_precoSemente.setText(holder.tv_precoSemente.getText().toString() + " " + semente.getPreco());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalheSemente.class);
            intent.putExtra("id", semente.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sementes.size();
    }

    public static class SementeViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nomeSemente;
        TextView tv_quantidadeSemente;
        TextView tv_precoSemente;

        public SementeViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nomeSemente = itemView.findViewById(R.id.tv_nomeSemente);
            tv_quantidadeSemente = itemView.findViewById(R.id.tv_quantidadeSemente);
            tv_precoSemente = itemView.findViewById(R.id.tv_precoSemente);
        }
    }
}
