package edu.ifam.br.ecosemente.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.ifam.br.ecosemente.R;
import edu.ifam.br.ecosemente.entity.ItemVenda;
import edu.ifam.br.ecosemente.entity.Semente;

public class ItemVendaAdapter extends RecyclerView.Adapter<ItemVendaViewHolder> {

    private List<Semente> sementes;
    private List<ItemVenda> itemVendas;
    private Context context;

    public ItemVendaAdapter(List<Semente> sementes, List<ItemVenda> itemVendas, Context context) {
        this.sementes = sementes;
        this.itemVendas = itemVendas;
        this.context = context;
    }

    public void updateItemList(List<ItemVenda> newItemList) {
        this.itemVendas = newItemList;
        notifyDataSetChanged(); // Atualiza o RecyclerView
    }

    @NonNull
    @Override
    public ItemVendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venda, parent, false);
        return new ItemVendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemVendaViewHolder holder, int position) {
        ItemVenda itemVenda = itemVendas.get(position);

        // Configura o Adapter para o Spinner com personalização da cor do texto
        ArrayAdapter<Semente> spinnerAdapter = new ArrayAdapter<Semente>(
                context,
                android.R.layout.simple_spinner_item,
                sementes
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                // Personaliza o texto do item selecionado
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    textView.setTextColor(context.getResources().getColor(R.color.green)); // Cor personalizada
                    textView.setTextSize(16); // Tamanho do texto
                }
                return view;
            }
        };

        // Configura o layout do dropdown
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(spinnerAdapter);

        // Define a seleção do Spinner, se houver uma semente já associada ao item
        if (itemVenda.getSemente() != null) {
            int spinnerPosition = sementes.indexOf(itemVenda.getSemente());
            if (spinnerPosition >= 0) { // Verifica se a semente existe na lista
                holder.spinner.setSelection(spinnerPosition);
            }
        }

        // Listener do Spinner para atualizar o objeto ItemVenda
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Semente sementeSelecionada = sementes.get(position);
                if (itemVenda.getSemente() == null || !itemVenda.getSemente().equals(sementeSelecionada)) {
                    itemVenda.setSemente(sementeSelecionada);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nenhuma ação necessária
            }
        });

        // Configura os valores do EditText e TextView
        holder.etQuantidade.setText(String.valueOf(itemVenda.getQuantidade()));
        holder.tvPreco.setText(String.format("R$ %.2f", itemVenda.getPrecoItem()));

        // Adiciona um Listener para capturar mudanças no EditText de quantidade, se necessário
        holder.etQuantidade.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                try {
                    int quantidade = Integer.parseInt(holder.etQuantidade.getText().toString());
                    itemVenda.setQuantidade(quantidade);
                } catch (NumberFormatException e) {
                    holder.etQuantidade.setText(String.valueOf(itemVenda.getQuantidade()));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemVendas.size();
    }
}
