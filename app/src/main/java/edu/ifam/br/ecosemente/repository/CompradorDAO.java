package edu.ifam.br.ecosemente.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.ifam.br.ecosemente.entity.Comprador;

public class CompradorDAO {

    private SQLiteDatabase sqLiteDatabase;

    private CompradorDAO(){}

    public CompradorDAO(Context context){
        BDEcoSemente bdEcoSemente = new BDEcoSemente(context);
        this.sqLiteDatabase = bdEcoSemente.getWritableDatabase();
    }

    // CREATE
    public long inserir(Comprador comprador) {
        ContentValues values = new ContentValues();
        values.put("cpf_cnpj", comprador.getCpfCnpj());
        values.put("email", comprador.getEmail());
        values.put("nome", comprador.getNome());
        values.put("telefone", comprador.getTelefone());
        return sqLiteDatabase.insert("comprador", null, values);
    }

    // READ
    public List<Comprador> listarTodos() {
        List<Comprador> lista = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("comprador", null, null, null, null, null, "nome ASC");

        if (cursor.moveToFirst()) {
            do {
                Comprador c = new Comprador();
                c.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                c.setCpfCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cpf_cnpj")));
                c.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                c.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                c.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
                lista.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // UPDATE
    public int atualizar(Comprador comprador) {
        ContentValues values = new ContentValues();
        values.put("cpf_cnpj", comprador.getCpfCnpj());
        values.put("email", comprador.getEmail());
        values.put("nome", comprador.getNome());
        values.put("telefone", comprador.getTelefone());
        return sqLiteDatabase.update("comprador", values, "id = ?", new String[]{String.valueOf(comprador.getId())});
    }
    // DELETE
    public int deletar(long id) {
        return sqLiteDatabase.delete("comprador", "id = ?", new String[]{String.valueOf(id)});
    }

    // BUSCAR POR ID
    public Comprador buscarPorId(long id) {
        Cursor cursor = sqLiteDatabase.query("comprador", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Comprador c = new Comprador();
            c.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            c.setCpfCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cpf_cnpj")));
            c.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            c.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            c.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
            cursor.close();
            return c;
        }
        return null;
    }
}
