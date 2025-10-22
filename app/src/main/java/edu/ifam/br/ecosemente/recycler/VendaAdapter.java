package edu.ifam.br.ecosemente.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import edu.ifam.br.ecosemente.DetalheVendaActivity;
import edu.ifam.br.ecosemente.R;
import edu.ifam.br.ecosemente.entity.Venda;

public class VendaAdapter extends RecyclerView.Adapter<VendaViewHolder> {

    private List<Venda> vendas;
    private Context context;


    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public VendaAdapter(List<Venda> vendas, Context context) {
        this.vendas = vendas;
        this.context = context;
    }


    @NonNull
    @Override
    public VendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venda_lista, parent, false);
        return new VendaViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VendaViewHolder holder, int position) {
        Venda venda = vendas.get(position);

        holder.tv_nomeComprador.setText(holder.tv_nomeComprador.getText().toString() + " " + venda.getComprador().getNome());

        String data = DATE_FORMAT.format(venda.getDataVenda());
        holder.tv_dataVenda.setText(holder.tv_dataVenda.getText().toString() + " " + data);


        holder.tv_valorVenda.setText(holder.tv_valorVenda.getText().toString() + " " + venda.getValorTotal());


        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetalheVendaActivity.class);
            intent.putExtra("id", venda.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return vendas.size();
    }
}
