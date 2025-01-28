package edu.ifam.br.ecosemente.recycler;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
    private TextView tvValorTotal;



    public ItemVendaAdapter(List<Semente> sementes, List<ItemVenda> itemVendas, Context context, TextView tvValorTotal) {
        this.sementes = sementes;
        this.itemVendas = itemVendas;
        this.context = context;
        this.tvValorTotal = tvValorTotal;
    }

    public void updateItemList(List<ItemVenda> newItemList) {
        this.itemVendas = newItemList;
        notifyDataSetChanged(); // Atualiza o RecyclerView
    }
    public List<ItemVenda> getItemVendas() {
        return itemVendas;
    }
    public float getPrecoTotal(){
        float precoTotal =0;
        for(ItemVenda itemVenda : itemVendas){
            precoTotal += itemVenda.getPrecoItem();
        }

        return  precoTotal;
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
            public boolean isEnabled(int position){
                return position != 0;
            }
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
            Semente sementeNoItemVenda = new Semente();
            for(Semente semente : sementes){
                if(itemVenda.getSemente().getNome().equals(semente.getNome())){
                    sementeNoItemVenda = semente;
                }
            }
            int spinnerPosition = sementes.indexOf(sementeNoItemVenda);
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
        // Adiciona um Listener para capturar mudanças no EditText de quantidade, se necessário
        holder.etQuantidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não é necessário implementar
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int quantidade = Integer.parseInt(s.toString());
                    itemVenda.setQuantidade(quantidade);

                    // Recalcula o preço com base na quantidade
                    if (itemVenda.getSemente() != null) {
                        float precoUnitario = itemVenda.getSemente().getPreco(); // Obtém o preço da semente
                        float precoItem = precoUnitario * quantidade;

                        // Atualiza o TextView do preço do item
                        holder.tvPreco.setText(String.format("R$ %.2f", precoItem));
                    }

                    updateValorTotal();



                } catch (NumberFormatException e) {
                    // Se o campo de quantidade estiver vazio ou inválido
                    itemVenda.setQuantidade(0);
                    holder.tvPreco.setText("R$ 0.00");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não é necessário implementar
            }
        });



    }


    @Override
    public int getItemCount() {
        return itemVendas.size();
    }

    private void updateValorTotal() {
        float valorTotal = 0.0f;  // Use float aqui

        for (ItemVenda item : itemVendas) {
            if (item.getSemente() != null) {
                valorTotal += item.getSemente().getPreco() * item.getQuantidade();  // Certifique-se de multiplicar corretamente com float
            }
        }

        // Atualiza o valor total no TextView
        tvValorTotal.setText(String.format("Valor Total: %.2f", valorTotal)); // Usando %.2f para formatar como float com 2 casas decimais
    }
}
