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

import edu.ifam.br.ecosemente.dto.CompradorDTO;
import edu.ifam.br.ecosemente.entity.Comprador;
import edu.ifam.br.ecosemente.interfaces.CompradorAPI;
import edu.ifam.br.ecosemente.recycler.CompradorAdapter;
import edu.ifam.br.ecosemente.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCompradorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CompradorAdapter compradorAdapter;
    private ProgressBar pbCompradorList;
    private CompradorAPI compradorAPI;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_comprador);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setContentView(R.layout.activity_list_comprador);

        recyclerView = findViewById(R.id.recyclerViewComprador);
        pbCompradorList = findViewById(R.id.pbCompradorList);
        pbCompradorList.setVisibility(View.INVISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        compradorAPI = RetrofitService.createService(CompradorAPI.class);

        context = this;

    }

    @Override
    protected void onStart() {
        super.onStart();
        getComprador();
    }

    public void backHome(android.view.MenuItem item){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addCompradorOnClick(android.view.MenuItem item){
        Intent intent = new Intent(this, DetalheCompradorActivity.class);
        startActivity(intent);
    }

    private void getComprador(){
        Call<List<CompradorDTO>> call = compradorAPI.getComprador();

        pbCompradorList.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<CompradorDTO>>() {
            @Override
            public void onResponse(Call<List<CompradorDTO>> call, Response<List<CompradorDTO>> response) {
                List<Comprador> compradores = new ArrayList<>();

                if (response.isSuccessful() && response.body() != null) {
                    List<CompradorDTO> compradorDTOS = response.body();
                    for (CompradorDTO compradorDTO : compradorDTOS) {
                        compradores.add(compradorDTO.getComprador());
                    }
                } else {
                    String codigoErro = "Erro: " + response.code();
                    Toast.makeText(getApplicationContext(), codigoErro, Toast.LENGTH_LONG).show();
                }
                compradorAdapter = new CompradorAdapter(compradores, context);
                recyclerView.setAdapter(compradorAdapter);

                pbCompradorList.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(Call<List<CompradorDTO>> call, Throwable t) {
                    String failureMessage = "Falha de acesso: " + t.getMessage();
                    Toast.makeText(getApplicationContext(), failureMessage, Toast.LENGTH_LONG).show();
                    pbCompradorList.setVisibility(View.INVISIBLE);
            }

        });
    }


}