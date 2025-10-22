package edu.ifam.br.ecosemente;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import edu.ifam.br.ecosemente.entity.NomeCientifico;
import edu.ifam.br.ecosemente.repository.NomeCientificoDAO;

public class NomeCientificoActivity extends AppCompatActivity {

    private EditText etNome;
    private Button btnSalvar, btnLimpar, btnDeletar;
    private ListView lvNomes;
    private ProgressBar pb;

    private NomeCientificoDAO dao;
    private List<NomeCientifico> listaNomesCientificos;
    private ArrayAdapter<NomeCientifico> adapter;

    // Guarda o item selecionado no ListView para Update/Delete
    private NomeCientifico nomeCientificoSelecionado = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crud_nome_cientifico);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Conectar componentes do layout
        etNome = findViewById(R.id.etNomeCientificoInput);
        btnSalvar = findViewById(R.id.btnNomeCientificoSalvar);
        btnLimpar = findViewById(R.id.btnNomeCientificoLimpar);
        btnDeletar = findViewById(R.id.btnNomeCientificoDeletar);
        lvNomes = findViewById(R.id.lvNomesCientificos);
        pb = findViewById(R.id.pbCrudNomeCientifico);

        // 2. Inicializar DAO e lista
        dao = new NomeCientificoDAO(this);
        listaNomesCientificos = new ArrayList<>();

        // 3. Configurar o Adapter do ListView
        // Usamos o 'toString()' da classe NomeCientifico para exibir o texto
        adapter = new ArrayAdapter<>(this, R.layout.list_item_verde, listaNomesCientificos);
        lvNomes.setAdapter(adapter);

        // 4. Configurar Listeners de Clique
        configurarListeners();

        // 5. Estado inicial dos botões
        limparCampos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dao.open(); // Abre a conexão com o banco
        carregarLista(); // Carrega a lista sempre que a tela ficar visível
    }

    @Override
    protected void onPause() {
        super.onPause();
        dao.close(); // Fecha a conexão com o banco
    }

    private void configurarListeners() {
        // --- Ação Salvar/Atualizar ---
        btnSalvar.setOnClickListener(v -> salvar());

        // --- Ação Limpar ---
        btnLimpar.setOnClickListener(v -> limparCampos());

        // --- Ação Deletar ---
        btnDeletar.setOnClickListener(v -> deletar());

        // --- Ação de clique no Item da Lista (para Update/Delete) ---
        lvNomes.setOnItemClickListener((parent, view, position, id) -> {
            // Pega o objeto clicado
            nomeCientificoSelecionado = listaNomesCientificos.get(position);

            // Preenche o campo de texto
            etNome.setText(nomeCientificoSelecionado.getNome());

            // Ajusta os botões
            btnSalvar.setText("Atualizar");
            btnDeletar.setEnabled(true);
        });
    }

    private void carregarLista() {
        pb.setVisibility(View.VISIBLE);
        listaNomesCientificos.clear();
        listaNomesCientificos.addAll(dao.listarTodos());
        adapter.notifyDataSetChanged(); // Avisa o adapter que os dados mudaram
        pb.setVisibility(View.GONE);
    }

    private void salvar() {
        String nome = etNome.getText().toString().trim();
        if (nome.isEmpty()) {
            Toast.makeText(this, "Por favor, digite um nome.", Toast.LENGTH_SHORT).show();
            return;
        }

        pb.setVisibility(View.VISIBLE);

        if (nomeCientificoSelecionado == null) {
            // --- Lógica de INSERIR (Create) ---
            long resultado = dao.inserir(nome);
            if (resultado > 0) {
                Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
            } else if (resultado == -1) {
                Toast.makeText(this, "Erro: Este nome já existe.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao salvar.", Toast.LENGTH_SHORT).show();
            }

        } else {
            // --- Lógica de ATUALIZAR (Update) ---
            nomeCientificoSelecionado.setNome(nome);
            int rowsAfetadas = dao.atualizar(nomeCientificoSelecionado);
            if (rowsAfetadas > 0) {
                Toast.makeText(this, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao atualizar.", Toast.LENGTH_SHORT).show();
            }
        }

        pb.setVisibility(View.GONE);
        limparCampos();
        carregarLista();
    }

    private void deletar() {
        if (nomeCientificoSelecionado == null) {
            return; // Nenhuma ação se nada estiver selecionado
        }

        pb.setVisibility(View.VISIBLE);
        try {
            // --- Lógica de DELETAR (Delete) ---
            int rowsAfetadas = dao.deletar(nomeCientificoSelecionado);
            if (rowsAfetadas > 0) {
                Toast.makeText(this, "Deletado com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao deletar.", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteConstraintException e) {
            // Captura a exceção se a FOREIGN KEY impedir a exclusão
            Toast.makeText(this, "Erro: Nome está em uso por uma semente.", Toast.LENGTH_LONG).show();
        }

        pb.setVisibility(View.GONE);
        limparCampos();
        carregarLista();
    }

    /**
     * Reseta a tela para o estado inicial (Inserção).
     */
    private void limparCampos() {
        nomeCientificoSelecionado = null;
        etNome.setText("");
        btnSalvar.setText("Salvar");
        btnDeletar.setEnabled(false); // Desabilita o "Deletar"
        etNome.requestFocus();
    }

}