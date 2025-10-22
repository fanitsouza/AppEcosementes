package edu.ifam.br.ecosemente.repository;

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
    public long inserir(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return -1; // Não insere nulo ou vazio
        }
        ContentValues values = new ContentValues();
        values.put("nome", nome.trim());

        // Usamos CONFLICT_IGNORE para que ele retorne -1 caso a restrição UNIQUE falhe
        return db.insertWithOnConflict("nome_cientifico", null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    /**
     * Lista todos os nomes científicos em ordem alfabética.
     */
    public List<NomeCientifico> listarTodos() {
        List<NomeCientifico> lista = new ArrayList<>();
        Cursor cursor = db.query("nome_cientifico", new String[]{"id", "nome"},
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

    private NomeCientifico cursorToNomeCientifico(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
        String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
        return new NomeCientifico(id, nome);
    }
}
