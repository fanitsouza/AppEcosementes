/*package edu.ifam.br.ecosemente.recycler;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.ifam.br.ecosemente.entity.Semente;

public class SementeAdapter extends RecyclerView.Adapter<SementeAdapter.SementeViewHolder> {
    private List<Semente> sementes;
    private Context context;

    public FornecedorAdapter(List<Semente> fornecedores, Context context) {
        this.sementes = fornecedores;
        this.context = context;
    }

    @NonNull
    @Override
    //Cria a ligação com layout que ira conter o Linear Layout com as informações do fornecedor
    public SementeAdapter.SementeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fornecedor, parent, false);
        return new SementeViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SementeAdapter.SementeViewHolder holder, int position) {
        Fornecedor fornecedor = sementes.get(position);
        holder.tvCodigo.setText(holder.tvCodigo.getText().toString() + " " + String.valueOf(fornecedor.getCodigo()));
        holder.tvNome.setText(holder.tvNome.getText().toString() + " " + fornecedor.getNome());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FornecedorDetalhesActivity.class);
            intent.putExtra("id", fornecedor.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sementes.size();
    }

    public static class SementeViewHolder extends RecyclerView.ViewHolder {
        TextView tvCodigo;
        TextView tvNome;

        public SementeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvNome = itemView.findViewById(R.id.tvNome);
        }
    }
}*/
