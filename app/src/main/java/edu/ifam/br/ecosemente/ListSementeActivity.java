package edu.ifam.br.ecosemente;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText; // IMPORT ADICIONADO
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.ifam.br.ecosemente.entity.Semente;
import edu.ifam.br.ecosemente.recycler.SementeAdapter;
import edu.ifam.br.ecosemente.repository.SementeDAO;
// Removidos imports de Retrofit/SementeAPI/SementeDTO (não usados aqui)

public class ListSementeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SementeAdapter sementeAdapter;
    private ProgressBar pbListSemente;
    private Context context;
    private SementeDAO sementeDAO;

    private EditText etBuscaSemente; // ADICIONADO
    private List<Semente> listaCompletaSementes = new ArrayList<>(); // ADICIONADO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this); // Removido
        setContentView(R.layout.activity_list_semente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            // return insets;
            // Simplificado para evitar problemas de layout
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewSemente);
        pbListSemente = findViewById(R.id.pbListSemente);
        etBuscaSemente = findViewById(R.id.etBuscaSemente); // ADICIONADO
        pbListSemente.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        context = this;
        sementeDAO = new SementeDAO(this);

        // MUDANÇA: Inicializa o Adapter UMA VEZ com a lista vazia
        sementeAdapter = new SementeAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(sementeAdapter);

        // MUDANÇA: Configura o listener da busca
        configurarBusca();
    }

    private void configurarBusca() {
        etBuscaSemente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrar(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filtrar(String texto) {
        List<Semente> listaFiltrada = new ArrayList<>();
        for (Semente semente : listaCompletaSementes) {
            // Filtra pelo nome popular
            if (semente.getNome().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(semente);
            }
        }
        sementeAdapter.filtrarLista(listaFiltrada);
    }

    @Override
    protected void onStart(){
        super.onStart();
        sementeDAO.open();
        getSementes(); // Carrega/Recarrega os dados do banco
        etBuscaSemente.setText(""); // Limpa a busca ao voltar
    }

    @Override
    protected void onStop() {
        super.onStop();
        sementeDAO.close();
    }

    public void addSementeOnClick(android.view.MenuItem item){
        Intent intent = new Intent(this, DetalheSementeActivity.class);
        startActivity(intent);
    }

    public void backHome(android.view.MenuItem item){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void getSementes(){
        pbListSemente.setVisibility(View.VISIBLE);
        listaCompletaSementes.clear();
        listaCompletaSementes.addAll(sementeDAO.getSemente());

        // Atualiza o adapter com a lista completa
        sementeAdapter.filtrarLista(listaCompletaSementes);

        pbListSemente.setVisibility(View.INVISIBLE);
    }
}