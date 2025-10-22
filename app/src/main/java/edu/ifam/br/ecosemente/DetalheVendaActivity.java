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
import edu.ifam.br.ecosemente.repository.CompradorDAO;
import edu.ifam.br.ecosemente.repository.ItemVendaDAO;
import edu.ifam.br.ecosemente.repository.SementeDAO;
import edu.ifam.br.ecosemente.repository.VendaDAO;
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
    private Button btnCancelaVenda;
    private List<ItemVenda> itemVendas;
    private List<Comprador> compradores;
    private List<Semente> sementesList;
    private CompradorAPI compradorAPI;
    private SementeAPI sementeAPI;
    private VendaAPI vendaAPI;
    private Context context;
    private long id;
    private String cpfCnpj;
    private VendaDAO vendaDAO;
    private ItemVendaDAO itemVendaDAO;
    private SementeDAO sementeDAO;
    private CompradorDAO compradorDAO;

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

        vendaDAO = new VendaDAO(this);
        itemVendaDAO = new ItemVendaDAO(this);
        compradorDAO = new CompradorDAO(this);
        sementeDAO = new SementeDAO(this);


        tvComprador = findViewById(R.id.tv_detalhaVendaComprador);
        tvValorTotal = findViewById(R.id.tv_detalhaVendaValorTotal);

        spinnerComprador = findViewById(R.id.spinnerCompradores);


        recyclerView = findViewById(R.id.recyclerViewItemVenda);
        pbDetalheVenda = findViewById(R.id.pbDetalheVenda);
        scrollView = findViewById(R.id.svDetalheVenda);
        btnAdditem = findViewById(R.id.btnAddItem);

        btnDeletaVenda = findViewById(R.id.btnDeletarVenda);
        btnConfirmaVenda = findViewById(R.id.btnConfirmarVenda);
        btnCancelaVenda = findViewById(R.id.btnCancelaVenda);

        btnDeletaVenda.setVisibility(View.INVISIBLE);
        pbDetalheVenda.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.INVISIBLE);

        compradorAPI = RetrofitService.createService(CompradorAPI.class);
        sementeAPI = RetrofitService.createService(SementeAPI.class);
        vendaAPI = RetrofitService.createService(VendaAPI.class);

        btnAdditem.setOnClickListener(v->{
            addItem();
        });

        System.out.println("Executando getComprador");
        getComprador();
        System.out.println("getComprador foi executado");
        getSementes();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemVendaAdapter = new ItemVendaAdapter(sementesList, itemVendas, this, tvValorTotal);
        recyclerView.setAdapter(itemVendaAdapter);

        btnCancelaVenda.setOnClickListener(v->{
            cancelOnClick();
        });

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
        if(id == 0){
            comprador = (Comprador) spinnerComprador.getSelectedItem();
        }else{
            comprador = compradores.stream()
                    .filter(c -> c.getCpfCnpj() != null && c.getCpfCnpj().equals(cpfCnpj))
                    .findFirst()
                    .orElse(null);

        }

        Venda venda = new Venda();
        venda.setDataVenda(new Date());
        System.out.println(comprador);
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
        pbDetalheVenda.setVisibility(View.VISIBLE);

        compradores.clear();
        Comprador placeHolder = new Comprador();
        placeHolder.setNome(getResources().getString(R.string.select_buyer));
        compradores.add(placeHolder);

        List<Comprador> compradorList = compradorDAO.listarTodos();
        for(Comprador comprador : compradorList){
            System.out.println("Listando Compradores: " + comprador);// novo método no DAO
        }
        compradores.addAll(compradorList);

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
        pbDetalheVenda.setVisibility(View.INVISIBLE);
    }


    public void cancelOnClick(){
        finish();
    }



    private void getSementes(){


        sementesList = new ArrayList<>();
        Semente placeHolder = new Semente();
        placeHolder.setNome(getResources().getString(R.string.select_seed));
        sementesList.add(placeHolder);

        List<Semente> sementes = sementeDAO.getSemente(); // novo método no DAO
        sementesList.addAll(sementes);

        itemVendaAdapter = new ItemVendaAdapter(sementesList, itemVendas, context,tvValorTotal);
        recyclerView.setAdapter(itemVendaAdapter);


    }

    private void getVenda(long id) {
        Venda venda = vendaDAO.buscarPorId(id);
        if (venda != null) {
            venda.setItens(itemVendaDAO.buscarItensPorVendaId(id));
            setVendaOnComponentes(venda);
            scrollView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Venda não encontrada", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void saveVenda() {
        Venda novaVenda = getVendaFromComponentes();
        novaVenda.setDataVenda(new Date());
        int vendaId = vendaDAO.inserir(novaVenda);
        if (vendaId > 0) {
            for (ItemVenda item : novaVenda.getItens()) {
                item.setVendaId(vendaId);
                itemVendaDAO.inserir(item);
            }
            Toast.makeText(this, "Venda salva com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar venda.", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateVenda(long id) {
        Venda vendaAtualizada = getVendaFromComponentes();
        vendaAtualizada.setId(id);

        if (vendaDAO.atualizar(vendaAtualizada) !=0) {
            itemVendaDAO.deletar(id); // limpa os itens antigos
            for (ItemVenda item : vendaAtualizada.getItens()) {
                item.setVendaId(id);
                itemVendaDAO.inserir(item);
            }
            Toast.makeText(this, "Venda atualizada com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao atualizar venda.", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteVenda(long id) {
        itemVendaDAO.deletar(id); // Deleta itens primeiro
        if (vendaDAO.deletar(id) != 0) {
            Toast.makeText(this, "Venda deletada com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao deletar venda.", Toast.LENGTH_SHORT).show();
        }
    }


}