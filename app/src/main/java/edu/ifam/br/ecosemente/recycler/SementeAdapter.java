package edu.ifam.br.ecosemente.recycler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri; // IMPORTAÇÃO ADICIONADA
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView; // IMPORTAÇÃO ADICIONADA
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.ifam.br.ecosemente.DetalheSementeActivity;
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
    public SementeAdapter.SementeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usa o NOVO layout (item_semente.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.item_semente, parent, false);
        return new SementeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SementeAdapter.SementeViewHolder holder, int position) {
        Semente semente = sementes.get(position);

        holder.tvNomePopular.setText("Nome popular: " + semente.getNome());
        holder.tvTipoCultivo.setText("Tipo: " + semente.getTipoCultivo());

        try {
            if (semente.getCaminhoImagem() != null && !semente.getCaminhoImagem().isEmpty()) {
                Uri imageUri = Uri.parse(semente.getCaminhoImagem());

                // --- LINHA REMOVIDA ---
                // context.getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                // --- FIM DA REMOÇÃO ---

                holder.ivSemente.setImageURI(imageUri);
            } else {
                holder.ivSemente.setImageResource(R.drawable.design_sem_nome_removebg_preview);
            }
        } catch (SecurityException e) {
            Log.e("SementeAdapter", "Permission error loading image URI: " + semente.getCaminhoImagem(), e);
            holder.ivSemente.setImageResource(R.drawable.design_sem_nome_removebg_preview);
        } catch (Exception e) {
            Log.e("SementeAdapter", "Error loading image URI: " + semente.getCaminhoImagem(), e);
            holder.ivSemente.setImageResource(R.drawable.design_sem_nome_removebg_preview);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalheSementeActivity.class);
            intent.putExtra("id", semente.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sementes.size();
    }

    /**
     * MUDANÇA: ViewHolder atualizado para os novos IDs
     */
    public static class SementeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSemente;
        TextView tvNomePopular;
        TextView tvTipoCultivo;

        public SementeViewHolder(@NonNull View itemView) {
            super(itemView);
            // Encontra os componentes do NOVO item_semente.xml
            ivSemente = itemView.findViewById(R.id.ivItemSemente);
            tvNomePopular = itemView.findViewById(R.id.tvItemNomePopular);
            tvTipoCultivo = itemView.findViewById(R.id.tvItemTipoCultivo);
        }
    }

    /**
     * MUDANÇA: Método adicionado para a barra de pesquisa
     */
    public void filtrarLista(List<Semente> listaFiltrada) {
        this.sementes = listaFiltrada;
        notifyDataSetChanged();
    }
}