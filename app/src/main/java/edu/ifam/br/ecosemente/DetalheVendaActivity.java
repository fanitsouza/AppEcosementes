package edu.ifam.br.ecosemente;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.ifam.br.ecosemente.dto.CompradorDTO;
import edu.ifam.br.ecosemente.dto.SementeDTO;
import edu.ifam.br.ecosemente.dto.VendaInputDTO;
import edu.ifam.br.ecosemente.dto.VendaOutputDTO;
import edu.ifam.br.ecosemente.entity.Comprador;
import edu.ifam.br.ecosemente.entity.ItemVenda;
import edu.ifam.br.ecosemente.entity.Semente;
import edu.ifam.br.ecosemente.entity.Venda;
import edu.ifam.br.ecosemente.interfaces.CompradorAPI;
import edu.ifam.br.ecosemente.interfaces.SementeAPI;
import edu.ifam.br.ecosemente.interfaces.VendaAPI;
import edu.ifam.br.ecosemente.recycler.ItemVendaAdapter;
import edu.ifam.br.ecosemente.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalheVendaActivity extends AppCompatActivity {

    private TextView tvComprador;
    private TextView tvValorTotal;
    private Spinner spinnerComprador;
    private RecyclerView recyclerView;
    private ItemVendaAdapter itemVendaAdapter;
    private ProgressBar pbDetalheVenda;
    private ScrollView scrollView;
    private Button btnAdditem;
    private Button btnDeletaVenda;
    private Button btnConfirmaVenda;
    private List<ItemVenda> itemVendas;
    private List<Semente> sementesList;
    private List<Comprador> compradores;
    private CompradorAPI compradorAPI;
    private SementeAPI sementeAPI;
    private VendaAPI vendaAPI;
    private Context context;
    private Long id;
    private String cpfCnpj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhe_venda);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //setContentView(R.layout.activity_detalhe_venda);
        context = this;

        itemVendas = new ArrayList<>();
        compradores = new ArrayList<>();
        cpfCnpj = "";


        tvComprador = findViewById(R.id.tv_detalhaVendaComprador);
        tvValorTotal = findViewById(R.id.tv_detalhaVendaValorTotal);

        spinnerComprador = findViewById(R.id.spinnerCompradores);


        recyclerView = findViewById(R.id.recyclerViewItemVenda);
        pbDetalheVenda = findViewById(R.id.pbDetalheVenda);
        scrollView = findViewById(R.id.svDetalheVenda);
        btnAdditem = findViewById(R.id.btnAddItem);

        btnDeletaVenda = findViewById(R.id.btnDeletarVenda);
        btnConfirmaVenda = findViewById(R.id.btnConfirmarVenda);

        btnDeletaVenda.setVisibility(View.INVISIBLE);
        pbDetalheVenda.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.INVISIBLE);

        compradorAPI = RetrofitService.createService(CompradorAPI.class);
        sementeAPI = RetrofitService.createService(SementeAPI.class);
        vendaAPI = RetrofitService.createService(VendaAPI.class);

        btnAdditem.setOnClickListener(v->{
            addItem();
        });


        getComprador();
        getSementes();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemVendaAdapter = new ItemVendaAdapter(sementesList, itemVendas, this, tvValorTotal);
        recyclerView.setAdapter(itemVendaAdapter);


        Intent intent = getIntent();
        if(intent.hasExtra("id")){
            id = intent.getLongExtra("id", 0);
            getVenda(id);
            btnDeletaVenda.setVisibility(View.VISIBLE);
            spinnerComprador.setVisibility(View.INVISIBLE);

            btnConfirmaVenda.setOnClickListener(v->{
                updateVenda(id);
            });

            btnDeletaVenda.setOnClickListener(v->{
                deleteVenda(id);
            });
        }else{
            scrollView.setVisibility(View.VISIBLE);
            tvComprador.setVisibility(View.INVISIBLE);
            btnConfirmaVenda.setOnClickListener(v -> {

                saveVenda();
            });

        }
    }



    private Venda getVendaFromComponentes(){
        Comprador comprador;
        System.out.println("Debug de getVendaFromComponentes: "+cpfCnpj);
        if(id == null){
            comprador = (Comprador) spinnerComprador.getSelectedItem();
        }else{
            comprador = compradores.stream()
                    .filter(c -> c.getCpfCnpj() != null && c.getCpfCnpj().equals(cpfCnpj))
                    .findFirst()
                    .orElse(null);

        }

        Venda venda = new Venda();
        venda.setDataVenda(new Date());
        venda.setComprador(comprador);
        // Obtenha os itens do adaptador
        List<ItemVenda> itensVenda = itemVendaAdapter.getItemVendas();

        // Valide e processe os itens
        for (ItemVenda item : itensVenda) {
            if (item.getSemente() == null) {
                Toast.makeText(this, "Selecione uma semente para cada item!", Toast.LENGTH_SHORT).show();
            }
            if (item.getQuantidade() <= 0) {
                Toast.makeText(this, "Informe uma quantidade válida!", Toast.LENGTH_SHORT).show();
            }
        }
        venda.setItens(itensVenda);
        return  venda;
    }



    private void setVendaOnComponentes(Venda venda) {
        tvComprador.setText(venda.getComprador().getNome());
        tvValorTotal.setText(String.format("Valor Total: %.2f", venda.getValorTotal()));
        cpfCnpj = venda.getComprador().getCpfCnpj();
        itemVendas = venda.getItens();

        if (itemVendaAdapter != null) {
            itemVendaAdapter.updateItemList(itemVendas);
        } else {
            Toast.makeText(context, "Adaptador de itens não inicializado.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addItem() {
        ItemVenda itemVenda = new ItemVenda(); // Cria um novo item
        itemVendas.add(itemVenda); // Adiciona o item à lista
        itemVendaAdapter.notifyItemInserted(itemVendas.size() - 1); // Notifica o adaptador que um novo item foi adicionado
    }




    private void getComprador() {
        Call<List<CompradorDTO>> call = compradorAPI.getComprador();

        pbDetalheVenda.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<CompradorDTO>>() {
            @Override
            public void onResponse(Call<List<CompradorDTO>> call, Response<List<CompradorDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    compradores.clear();
                    Comprador placeHolder = new Comprador();
                    placeHolder.setNome(getResources().getString(R.string.select_buyer));
                    compradores.add(placeHolder);
                    List<CompradorDTO> compradorDTOS = response.body();
                    for (CompradorDTO compradorDTO : compradorDTOS) {
                        compradores.add(compradorDTO.getComprador());
                    }
                    ArrayAdapter<Comprador> spinnerAdapter = new ArrayAdapter<Comprador>(
                            context,
                            android.R.layout.simple_spinner_item,
                            compradores
                    ){
                        @Override
                        public boolean isEnabled(int position){
                            return position != 0;
                        }

                        @Override
                        public View getView(int position, View viewConverter, ViewGroup parent){
                            View view = super.getDropDownView(position, viewConverter, parent);
                            TextView tv = (TextView) view;
                            if(position == 0){
                                tv.setTextColor(getResources().getColor(android.R.color.darker_gray));
                            }else{
                                tv.setTextColor(getResources().getColor(R.color.green));
                            }
                            return view;
                        }

                    };

                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerComprador.setAdapter(spinnerAdapter);
                    spinnerComprador.setSelection(0);

                } else {
                    String codigoErro = "Erro: " + response.code();
                    Toast.makeText(getApplicationContext(), codigoErro, Toast.LENGTH_LONG).show();
                }
                pbDetalheVenda.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<CompradorDTO>> call, Throwable t) {
                String failureMessage = "Falha de acesso: " + t.getMessage();
                Toast.makeText(getApplicationContext(), failureMessage, Toast.LENGTH_LONG).show();
                pbDetalheVenda.setVisibility(View.INVISIBLE);
            }
        });
    }



    private void getSementes(){
        Call<List<SementeDTO>> call = sementeAPI.getSemente();

        pbDetalheVenda.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<List<SementeDTO>>() {
            @Override
            public void onResponse(Call<List<SementeDTO>> call, Response<List<SementeDTO>> response) {

                List<Semente> sementes = new ArrayList<>();

                if(response.isSuccessful() && response.body() != null){
                    Semente placeHolder = new Semente();
                    placeHolder.setNome(getResources().getString(R.string.select_seed));
                    sementes.add(placeHolder);
                    List<SementeDTO> sementeDTOS = response.body();
                    for(SementeDTO sementeDTO : sementeDTOS){
                        sementes.add(sementeDTO.getSemente());
                    }
                }else{
                    String codigoErro = "Erro: " + response.code();
                    Toast.makeText(getApplicationContext(), codigoErro, Toast.LENGTH_LONG).show();
                }

                itemVendaAdapter = new ItemVendaAdapter(sementes,itemVendas, context, tvValorTotal);
                recyclerView.setAdapter(itemVendaAdapter);

                pbDetalheVenda.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<SementeDTO>> call, Throwable t) {
                String failureMessage = "Falha de acesso: " + t.getMessage();
                Toast.makeText(getApplicationContext(), failureMessage, Toast.LENGTH_LONG).show();
                pbDetalheVenda.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void getVenda(Long id){
        Call<VendaOutputDTO> call = vendaAPI.getVenda(id);

        pbDetalheVenda.setVisibility(View.VISIBLE);

         call.enqueue(new Callback<VendaOutputDTO>() {
             @Override
             public void onResponse(Call<VendaOutputDTO> call, Response<VendaOutputDTO> response) {
                 Venda venda = new Venda();
                 if(response.isSuccessful() && response.body() != null){
                     VendaOutputDTO vendaOutputDTO = response.body();

                     venda = vendaOutputDTO.getVenda();

                 }else{
                     String codigoErro = "Erro: " + response.code();
                     Toast.makeText(getApplicationContext(), codigoErro, Toast.LENGTH_LONG).show();
                 }
                 cpfCnpj = venda.getComprador().getCpfCnpj();
                 System.out.println(cpfCnpj);
                 setVendaOnComponentes(venda);
                 pbDetalheVenda.setVisibility(View.INVISIBLE);
                 scrollView.setVisibility(View.VISIBLE);
             }

             @Override
             public void onFailure(Call<VendaOutputDTO> call, Throwable t) {
                 String failureMessage = "Falha de acesso: " + t.getMessage();
                 Toast.makeText(getApplicationContext(), failureMessage, Toast.LENGTH_LONG).show();
                pbDetalheVenda.setVisibility(View.INVISIBLE);
             }
         });
    }

    public void saveVenda(){
        Call<VendaOutputDTO> call = vendaAPI.setVenda(new VendaInputDTO(getVendaFromComponentes()));
        pbDetalheVenda.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<VendaOutputDTO>() {
            @Override
            public void onResponse(Call<VendaOutputDTO> call, Response<VendaOutputDTO> response) {
                pbDetalheVenda.setVisibility(View.INVISIBLE);
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(getApplicationContext(), "Venda adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Erro ao adicionar: " + response.code(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<VendaOutputDTO> call, Throwable t) {
                pbDetalheVenda.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Falha ao adicionar: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void updateVenda(Long id){
        Call<VendaOutputDTO> call = vendaAPI.updateVenda(id, new VendaInputDTO(getVendaFromComponentes()));
        pbDetalheVenda.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<VendaOutputDTO>() {
            @Override
            public void onResponse(Call<VendaOutputDTO> call, Response<VendaOutputDTO> response) {
                pbDetalheVenda.setVisibility(View.INVISIBLE);
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(getApplicationContext(), "Venda atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Erro ao atualizar: " + response.code(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<VendaOutputDTO> call, Throwable t) {
                pbDetalheVenda.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Falha ao adicionar: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void deleteVenda(Long id){
        Call<Void> call = vendaAPI.deleteVenda(id);
        pbDetalheVenda.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                pbDetalheVenda.setVisibility(View.INVISIBLE);
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Venda deleta com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Erro ao deletar: " + response.code(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                pbDetalheVenda.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Falha ao deletear: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

}