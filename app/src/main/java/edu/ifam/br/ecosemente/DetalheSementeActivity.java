package edu.ifam.br.ecosemente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.ifam.br.ecosemente.entity.Semente;
import edu.ifam.br.ecosemente.repository.SementeDAO;

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
    private SementeDAO sementeDAO;
    private long id;


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

        sementeDAO = new SementeDAO(this);

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

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sementeDAO.insert(getSemente());
                Toast.makeText(getApplicationContext(),"Semente Adicionada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btndelete.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();

        if(intent.hasExtra("id")){
            btndelete.setVisibility(View.VISIBLE);
            id = intent.getLongExtra("id",0);
            setSemente(sementeDAO.getSemente(id));

            btnConfirmar.setOnClickListener(v -> {
                sementeDAO.update(id, getSemente());
                Toast.makeText(getApplicationContext(),"Semente Atualizada",
                        Toast.LENGTH_SHORT).show();
                finish();

            });
        }
    }


    private Semente getSemente(){
        Semente semente = new Semente();
        semente.setNome(etNome.getText().toString());
        semente.setDescricao(etDescricao.getText().toString());
        semente.setEspecie(etEspecie.getText().toString());
        semente.setEpocaPlantio(etEpocaPlantio.getText().toString());
        semente.setTempoMedioColheita(Integer.parseInt(etTempoMedio.getText().toString()));
        semente.setQuantidade(Integer.parseInt(etQuantidade.getText().toString()));
        semente.setCuidado(etCuidado.getText().toString());
        semente.setPreco(Float.parseFloat(etCuidado.getText().toString().replace(",",".")));

        return semente;
    }

    private void setSemente(Semente semente) {
        etNome.setText(semente.getNome());
        etDescricao.setText(semente.getDescricao());
        etEspecie.setText(semente.getEspecie());
        etEpocaPlantio.setText(semente.getEpocaPlantio());
        etTempoMedio.setText(String.valueOf(semente.getTempoMedioColheita()));
        etQuantidade.setText(String.valueOf(semente.getQuantidade()));
        etCuidado.setText(semente.getCuidado());
        etPreco.setText(String.valueOf(semente.getPreco()).replace(".",","));
    }


    public void btnDeleteSementeOnClick(View view){
        sementeDAO.delete(id);
        Toast.makeText(this,"Semente Excluída",
                Toast.LENGTH_SHORT).show();
        finish();

    }

    public void btnClearSementeOnClick(View view){
        setSemente(new Semente());
    }
}

