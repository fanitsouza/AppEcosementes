package edu.ifam.br.ecosemente.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.ifam.br.ecosemente.entity.NomeCientifico;

public class NomeCientificoDAO {

    private BDEcoSemente helper;
    private SQLiteDatabase db;

    public NomeCientificoDAO(Context context) {
        helper = new BDEcoSemente(context);
    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    /**
     * Insere um novo nome científico.
     * Retorna -1 se o nome já existir (devido à restrição UNIQUE) ou se houver erro.
     */
    public long inserir(String nomeCientifico, String nomePopular) {
        if (nomeCientifico == null || nomeCientifico.trim().isEmpty()) {
            return -1; // Não insere nulo ou vazio
        }
        ContentValues values = new ContentValues();
        values.put("nome", nomeCientifico.trim());
        values.put("nome_popular", nomePopular != null ? nomePopular.trim() : null);

        // Usamos CONFLICT_IGNORE para que ele retorne -1 caso a restrição UNIQUE falhe
        return db.insertWithOnConflict("nome_cientifico", null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    /**
     * Lista todos os nomes científicos em ordem alfabética.
     */
    public List<NomeCientifico> listarTodos() {
        List<NomeCientifico> lista = new ArrayList<>();
        // Adiciona a nova coluna 'nome_popular'
        Cursor cursor = db.query("nome_cientifico", new String[]{"id", "nome", "nome_popular"},
                null, null, null, null, "nome ASC");

        while (cursor.moveToNext()) {
            lista.add(cursorToNomeCientifico(cursor));
        }
        cursor.close();
        return lista;
    }

    /**
     * Atualiza um nome científico existente.
     */
    public int atualizar(NomeCientifico nc) {
        ContentValues values = new ContentValues();
        values.put("nome", nc.getNome().trim());
        values.put("nome_popular", nc.getNomePopular() != null ? nc.getNomePopular().trim() : null);

        return db.update("nome_cientifico", values, "id = ?",
                new String[]{String.valueOf(nc.getId())});
    }

    /**
     * Deleta um nome científico.
     * Pode lançar SQLiteConstraintException se o nome estiver em uso.
     */
    public int deletar(NomeCientifico nc) throws SQLiteConstraintException {
        return db.delete("nome_cientifico", "id = ?",
                new String[]{String.valueOf(nc.getId())});
    }

    // --- NOVOS MÉTODOS DE BUSCA ---

    /**
     * NOVO: Busca um NomeCientifico pelo ID.
     * Necessário para a tela de DetalheSemente (Modo Edição).
     */
    public NomeCientifico buscarPorId(long id) {
        Cursor cursor = db.query("nome_cientifico", new String[]{"id", "nome", "nome_popular"},
                "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        NomeCientifico nc = null;
        if (cursor.moveToFirst()) {
            nc = cursorToNomeCientifico(cursor);
        }
        cursor.close();
        return nc;
    }

    /**
     * NOVO: Busca um NomeCientifico pelo Nome Popular.
     * Esta é a lógica principal da sua nova funcionalidade.
     * Retorna o primeiro que encontrar (ou null).
     */
    public NomeCientifico buscarPorNomePopular(String nomePopular) {
        if (nomePopular == null || nomePopular.trim().isEmpty()) {
            return null;
        }

        // --- INÍCIO DA CORREÇÃO ---

        // 1. Mudamos de "=" para "LIKE"
        String selection = "UPPER(nome_popular) LIKE UPPER(?)";

        // 2. Adicionamos o "%" para buscar "começa com..."
        String[] selectionArgs = new String[]{nomePopular.trim() + "%"};

        Cursor cursor = db.query("nome_cientifico", new String[]{"id", "nome", "nome_popular"},
                selection,
                selectionArgs,
                null, null, null, "1"); // "1" limita a 1 resultado

        // --- FIM DA CORREÇÃO ---

        NomeCientifico nc = null;
        if (cursor.moveToFirst()) {
            nc = cursorToNomeCientifico(cursor);
        }
        cursor.close();
        return nc;
    }
    // --- FIM DOS NOVOS MÉTODOS ---

    @SuppressLint("Range")
    private NomeCientifico cursorToNomeCientifico(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
        String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
        // Adiciona a leitura da nova coluna
        String nomePopular = cursor.getString(cursor.getColumnIndexOrThrow("nome_popular"));

        return new NomeCientifico(id, nome, nomePopular);
    }
}