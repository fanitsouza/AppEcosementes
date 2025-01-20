package edu.ifam.br.ecosemente;

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

import edu.ifam.br.ecosemente.dto.SementeDTO;
import edu.ifam.br.ecosemente.entity.Semente;
import edu.ifam.br.ecosemente.interfaces.SementeAPI;
import edu.ifam.br.ecosemente.recycler.SementeAdapter;
import edu.ifam.br.ecosemente.repository.SementeDAO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListSemente extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SementeAdapter sementeAdapter;
    private ProgressBar pbListSemente;
    private SementeAPI sementeAPI;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_semente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setContentView(R.layout.activity_list_semente);
        recyclerView = findViewById(R.id.recyclerViewSemente);
        pbListSemente = findViewById(R.id.pbListSemente);
        pbListSemente.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        acessarApi();
    }

    protected void onStart(){
        super.onStart();
        getSementes();
    }

    public void addSementeOnClick(android.view.MenuItem item){
        Intent intent = new Intent(this, DetalheSemente.class);
        startActivity(intent);
    }

    public void backHome(android.view.MenuItem item){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void acessarApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.8:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sementeAPI = retrofit.create(SementeAPI.class);
    }

    private void getSementes(){
        Call<List<SementeDTO>> call = sementeAPI.getSemente();

        pbListSemente.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<List<SementeDTO>>() {
            @Override
            public void onResponse(Call<List<SementeDTO>> call, Response<List<SementeDTO>> response) {
                List<Semente> sementes = new ArrayList<>();

                if(response.isSuccessful() && response.body() != null){
                    List<SementeDTO> sementeDTOS = response.body();
                    for(SementeDTO sementeDTO : sementeDTOS){
                        sementes.add(sementeDTO.getSemente());
                    }
                }else{
                    String codigoErro = "Erro: " + response.code();
                    Toast.makeText(getApplicationContext(), codigoErro, Toast.LENGTH_LONG).show();
                }

                sementeAdapter = new SementeAdapter(sementes, getApplicationContext());
                recyclerView.setAdapter(sementeAdapter);

                pbListSemente.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<SementeDTO>> call, Throwable t) {
                String failureMessage = "Falha de acesso: " + t.getMessage();
                Toast.makeText(getApplicationContext(), failureMessage, Toast.LENGTH_LONG).show();
                pbListSemente.setVisibility(View.INVISIBLE);
            }
        });
    }

}