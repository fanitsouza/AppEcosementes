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
import edu.ifam.br.ecosemente.repository.CompradorDAO;
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
    private CompradorDAO compradorDAO;



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

        compradorDAO = new CompradorDAO(this);

        Intent intent = getIntent();
        if(intent.hasExtra("id")){
            id = intent.getLongExtra("id", 0);

            getComprador(id);

            btnDeletarComprador.setVisibility(View.VISIBLE);

            btnDeletarComprador.setOnClickListener(v -> {
                deleteComprador(id);
            });

            btnConfirmarComprador.setOnClickListener(v -> {
                updateComprador();
            });
            svDetalheComprador.setVisibility(View.VISIBLE);

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
        System.out.println(comprador);
        etNomeComprador.setText(comprador.getNome());
        etCpfCnpj.setText(comprador.getCpfCnpj());
        etTelefone.setText(comprador.getTelefone());
        etEmail.setText(comprador.getEmail());
    }

    public void btnClearCompradorOnClick(View view){
        setCompradorOnEditText(new Comprador());
    }

    private void getComprador(long id){
        try {
            Comprador comprador = compradorDAO.buscarPorId(id);
            System.out.println(comprador);
            setCompradorOnEditText(comprador);
        }catch (Exception e){
            Toast.makeText(this, "Erro ao salvar comprador: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();
        }


    }

    private void saveComprador(){
        try {
            Comprador comprador = getSementeFromEditText();
            compradorDAO.inserir(comprador);
            Toast.makeText(this, "Comprador salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao salvar comprador: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private void updateComprador(){
        try {
            Comprador comprador = getSementeFromEditText();
            comprador.setId(id);
            System.out.println(comprador);
            compradorDAO.atualizar(comprador);
            Toast.makeText(this, "Comprador atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao atualizar comprador: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void deleteComprador(long id){
        try {
            compradorDAO.deletar(id);
            Toast.makeText(this, "Comprador excluído com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao excluir comprador: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}