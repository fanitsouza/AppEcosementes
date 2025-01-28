package edu.ifam.br.ecosemente;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.ifam.br.ecosemente.dto.VendaOutputDTO;
import edu.ifam.br.ecosemente.entity.Venda;
import edu.ifam.br.ecosemente.interfaces.VendaAPI;
import edu.ifam.br.ecosemente.recycler.VendaAdapter;
import edu.ifam.br.ecosemente.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListVendaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VendaAdapter vendaAdapter;
    private ProgressBar pbVendaList;
    private VendaAPI vendaAPI;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_venda);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setContentView(R.layout.activity_list_venda);

        recyclerView = findViewById(R.id.recyclerViewVenda);
        pbVendaList = findViewById(R.id.pbListVenda);
        pbVendaList.setVisibility(View.INVISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        vendaAPI = RetrofitService.createService(VendaAPI.class);

        context = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getVendas();
    }

    public void backHomeVenda(android.view.MenuItem item){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addVendaOnClick(android.view.MenuItem item){
        Intent intent = new Intent(this, DetalheVendaActivity.class);
        startActivity(intent);
    }

    private void getVendas(){
        Call<List<VendaOutputDTO>> call = vendaAPI.getVenda();
        pbVendaList.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<VendaOutputDTO>>() {
            @Override
            public void onResponse(Call<List<VendaOutputDTO>> call, Response<List<VendaOutputDTO>> response) {
                List<Venda> vendas = new ArrayList<>();

                if (response.isSuccessful() && response.body() != null) {
                    List<VendaOutputDTO> vendaOutputDTOS = response.body();
                    for (VendaOutputDTO vendaOutputDTO : vendaOutputDTOS) {
                        vendas.add(vendaOutputDTO.getVenda());
                    }
                } else {
                    String codigoErro = "Erro: " + response.code();
                    Toast.makeText(getApplicationContext(), codigoErro, Toast.LENGTH_LONG).show();
                    System.out.println(codigoErro);
                }
                vendaAdapter = new VendaAdapter(vendas, context);
                recyclerView.setAdapter(vendaAdapter);

                pbVendaList.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(Call<List<VendaOutputDTO>> call, Throwable t) {
                    String failureMessage = "Falha de acesso: " + t.getMessage();
                    System.out.println(failureMessage);
                    Toast.makeText(getApplicationContext(), failureMessage, Toast.LENGTH_LONG).show();
                    pbVendaList.setVisibility(View.INVISIBLE);
                }
        });
    }

}