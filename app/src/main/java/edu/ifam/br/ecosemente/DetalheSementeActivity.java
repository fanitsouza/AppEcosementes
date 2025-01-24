package edu.ifam.br.ecosemente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.ifam.br.ecosemente.dto.SementeDTO;
import edu.ifam.br.ecosemente.entity.Semente;
import edu.ifam.br.ecosemente.interfaces.SementeAPI;
import edu.ifam.br.ecosemente.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalheSementeActivity extends AppCompatActivity {

    private EditText etNome;
    private EditText etDescricao;
    private EditText etEspecie;
    private EditText etEpocaPlantio;
    private EditText etTempoMedio;
    private EditText etQuantidade;
    private EditText etCuidado;
    private EditText etPreco;
    private Button btnConfirmar;
    private Button btndelete;
    private SementeAPI sementeAPI;
    private long id;
    private ScrollView svDetalheSemente;
    private ProgressBar pbDetalheSemente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhe_semente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sementeAPI = RetrofitService.createService(SementeAPI.class);

        etNome = findViewById(R.id.etDetalheSementeNomeSemente);
        etDescricao = findViewById(R.id.etDetalheSementeDescricaoSemente);
        etEspecie = findViewById(R.id.etDetalheSementeEspecieSemente);
        etEpocaPlantio = findViewById(R.id.etDetalheSementeEpocaPlantio);
        etTempoMedio = findViewById(R.id.etDetalheSementeTempoMedio);
        etQuantidade = findViewById(R.id.etDetalheSementeQuantidade);
        etCuidado = findViewById(R.id.etDetalheSementeCuidados);
        etPreco = findViewById(R.id.etDetalheSementePreco);

        btnConfirmar = findViewById(R.id.btnConfirmar);
        btndelete = findViewById(R.id.btnDeletar);

        svDetalheSemente = findViewById(R.id.svDetalheSemente);
        pbDetalheSemente = findViewById(R.id.pbDetalheSemente);

        svDetalheSemente.setVisibility(View.INVISIBLE);
        pbDetalheSemente.setVisibility(View.INVISIBLE);



        btndelete.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();

        if(intent.hasExtra("id")){
            id = intent.getLongExtra("id",0);

            getSemente(id);

            btndelete.setVisibility(View.VISIBLE);

            btndelete.setOnClickListener(v -> {
                deleteSemente(id);
            });

            btnConfirmar.setOnClickListener(v -> {
                updateSemente(id);
            });

        }else{
            svDetalheSemente.setVisibility(View.VISIBLE);
            btnConfirmar.setOnClickListener(v -> {
                saveSemente();
            });
        }
    }


    private Semente getSementeFromEditText(){
        Semente semente = new Semente();
        semente.setNome(etNome.getText().toString());
        semente.setDescricao(etDescricao.getText().toString());
        semente.setEspecie(etEspecie.getText().toString());
        semente.setEpocaPlantio(etEpocaPlantio.getText().toString());
        semente.setTempoMedioColheita(Integer.parseInt(etTempoMedio.getText().toString()));
        semente.setQuantidade(Integer.parseInt(etQuantidade.getText().toString()));
        semente.setCuidado(etCuidado.getText().toString());
        semente.setPreco(Float.parseFloat(etPreco.getText().toString().replace(",",".")));

        return semente;
    }

    private void setSementeOnEditText(Semente semente) {
        etNome.setText(semente.getNome());
        etDescricao.setText(semente.getDescricao());
        etEspecie.setText(semente.getEspecie());
        etEpocaPlantio.setText(semente.getEpocaPlantio());
        etTempoMedio.setText(String.valueOf(semente.getTempoMedioColheita()));
        etQuantidade.setText(String.valueOf(semente.getQuantidade()));
        etCuidado.setText(semente.getCuidado());
        etPreco.setText(String.valueOf(semente.getPreco()).replace(".",","));
    }


    public void btnClearSementeOnClick(View view){
        setSementeOnEditText(new Semente());
    }

    private void getSemente(Long id){
        Call<SementeDTO> call = sementeAPI.getSemente(id);

        pbDetalheSemente.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<SementeDTO>() {
            @Override
            public void onResponse(Call<SementeDTO> call, Response<SementeDTO> response) {

                Semente semente = new Semente();

                if(response.isSuccessful() && response.body() != null){
                    SementeDTO sementeDTO = response.body();

                    semente = sementeDTO.getSemente();

                }else{
                    String codigoErro = "Erro: " + response.code();
                    Toast.makeText(getApplicationContext(), codigoErro, Toast.LENGTH_LONG).show();

                }

                setSementeOnEditText(semente);
                pbDetalheSemente.setVisibility(View.INVISIBLE);
                svDetalheSemente.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<SementeDTO> call, Throwable t) {
                String failureMessage = "Falha de acesso: " + t.getMessage();
                Toast.makeText(getApplicationContext(), failureMessage, Toast.LENGTH_LONG).show();
                pbDetalheSemente.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void saveSemente(){
        Semente semente = getSementeFromEditText();

        pbDetalheSemente.setVisibility(View.VISIBLE);

        sementeAPI.setSemente(new SementeDTO(semente)).enqueue(new Callback<SementeDTO>() {
            @Override
            public void onResponse(Call<SementeDTO> call, Response<SementeDTO> response) {
                pbDetalheSemente.setVisibility(View.INVISIBLE);
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(getApplicationContext(), "Semente adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Erro ao adicionar: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SementeDTO> call, Throwable t) {
                pbDetalheSemente.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Falha ao adicionar: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateSemente(Long id){

        Semente semente = getSementeFromEditText();

        pbDetalheSemente.setVisibility(View.VISIBLE);

        sementeAPI.updateSemente(id, new SementeDTO(semente)).enqueue(new Callback<SementeDTO>() {
            @Override
            public void onResponse(Call<SementeDTO> call, Response<SementeDTO> response) {
                pbDetalheSemente.setVisibility(View.INVISIBLE);
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(getApplicationContext(), "Semente atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Erro ao atualizar: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SementeDTO> call, Throwable t) {
                pbDetalheSemente.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Falha ao atualizar: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void deleteSemente(Long id){
        pbDetalheSemente.setVisibility(View.VISIBLE);
        sementeAPI.deleteSemente(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                pbDetalheSemente.setVisibility(View.INVISIBLE);

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Semente Excluída", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Erro ao excluir: " + response.code(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Falha ao excluir: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}

