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

import edu.ifam.br.ecosemente.dto.CompradorDTO;
import edu.ifam.br.ecosemente.entity.Comprador;
import edu.ifam.br.ecosemente.entity.Semente;
import edu.ifam.br.ecosemente.interfaces.CompradorAPI;
import edu.ifam.br.ecosemente.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalheCompradorActivity extends AppCompatActivity {

    private ScrollView svDetalheComprador;
    private EditText etNomeComprador;
    private EditText etCpfCnpj;
    private EditText etTelefone;
    private EditText etEmail;
    private ProgressBar pbDetalheComprador;
    private Button btnLimparComprador;
    private Button btnDeletarComprador;
    private Button btnConfirmarComprador;
    private CompradorAPI compradorAPI;
    private long id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhe_comprador);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNomeComprador = findViewById(R.id.etDetalheCompradorNomeComprador);
        etCpfCnpj = findViewById(R.id.etDetalheCompradorCpfCnpjComprador);
        etTelefone = findViewById(R.id.etDetalheCompradorTelefoneComprador);
        etEmail = findViewById(R.id.etDetalheCompradorEmailComprador);

        svDetalheComprador = findViewById(R.id.svDetalheComprador);

        pbDetalheComprador = findViewById(R.id.pbDetalheComprador);

        btnConfirmarComprador = findViewById(R.id.btnConfirmarComprador);
        btnDeletarComprador = findViewById(R.id.btnDeletarComprador);
        btnLimparComprador = findViewById(R.id.btnLimparComprador);

        svDetalheComprador.setVisibility(View.INVISIBLE);
        pbDetalheComprador.setVisibility(View.INVISIBLE);
        btnDeletarComprador.setVisibility(View.INVISIBLE);

        compradorAPI = RetrofitService.createService(CompradorAPI.class);

        Intent intent = getIntent();
        if(intent.hasExtra("id")){
            id = intent.getLongExtra("id", 0);

            getComprador(id);

            btnDeletarComprador.setVisibility(View.VISIBLE);

            btnDeletarComprador.setOnClickListener(v -> {
                deleteComprador(id);
            });

            btnConfirmarComprador.setOnClickListener(v -> {
                updateComprador(id);
            });

        }else{
            svDetalheComprador.setVisibility(View.VISIBLE);
            btnConfirmarComprador.setOnClickListener(v -> {
                saveComprador();
            });
        }
    }


    private Comprador getSementeFromEditText(){
        Comprador comprador = new Comprador();
        comprador.setNome(etNomeComprador.getText().toString());
        comprador.setCpfCnpj(etCpfCnpj.getText().toString());
        comprador.setTelefone(etTelefone.getText().toString());
        comprador.setEmail(etEmail.getText().toString());
        return comprador;
    }

    private void setCompradorOnEditText(Comprador comprador){
        etNomeComprador.setText(comprador.getNome());
        etCpfCnpj.setText(comprador.getCpfCnpj());
        etTelefone.setText(comprador.getTelefone());
        etEmail.setText(comprador.getEmail());
    }

    public void btnClearCompradorOnClick(View view){
        setCompradorOnEditText(new Comprador());
    }

    private void getComprador(Long id){
        Call<CompradorDTO> call =  compradorAPI.getComprador(id);

        pbDetalheComprador.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<CompradorDTO>() {
            @Override
            public void onResponse(Call<CompradorDTO> call, Response<CompradorDTO> response) {

                Comprador comprador = new Comprador();

                if(response.isSuccessful() && response.body() != null){
                    CompradorDTO compradorDTO = response.body();

                    comprador = compradorDTO.getComprador();
                }else{
                    String codigoErro = "Erro: " + response.code();
                    Toast.makeText(getApplicationContext(), codigoErro, Toast.LENGTH_LONG).show();
                }

                setCompradorOnEditText(comprador);
                pbDetalheComprador.setVisibility(View.INVISIBLE);
                svDetalheComprador.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<CompradorDTO> call, Throwable t) {
                String failureMessage = "Falha de acesso: " + t.getMessage();
                Toast.makeText(getApplicationContext(), failureMessage, Toast.LENGTH_LONG).show();
                pbDetalheComprador.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void saveComprador(){
        Comprador comprador = getSementeFromEditText();

        pbDetalheComprador.setVisibility(View.VISIBLE);

        compradorAPI.createComprador(new CompradorDTO(comprador)).enqueue(new Callback<CompradorDTO>() {
            @Override
            public void onResponse(Call<CompradorDTO> call, Response<CompradorDTO> response) {
                pbDetalheComprador.setVisibility(View.INVISIBLE);
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(getApplicationContext(), "Comprador adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Erro ao adicionar: " + response.code(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CompradorDTO> call, Throwable t) {
                pbDetalheComprador.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Falha ao adicionar: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateComprador(Long id){
        Comprador comprador = getSementeFromEditText();

        pbDetalheComprador.setVisibility(View.VISIBLE);

        compradorAPI.updateComprador(id,new CompradorDTO(comprador)).enqueue(new Callback<CompradorDTO>() {
            @Override
            public void onResponse(Call<CompradorDTO> call, Response<CompradorDTO> response) {
                pbDetalheComprador.setVisibility(View.INVISIBLE);
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(getApplicationContext(), "Comprador atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Erro ao atualizar: " + response.code(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CompradorDTO> call, Throwable t) {
                pbDetalheComprador.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Falha ao atualizar: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteComprador(Long id){
        pbDetalheComprador.setVisibility(View.VISIBLE);
        compradorAPI.deleteComprador(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                pbDetalheComprador.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Comprador Excluído", Toast.LENGTH_SHORT).show();
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