package edu.ifam.br.ecosemente.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.ifam.br.ecosemente.entity.ItemVenda;
import edu.ifam.br.ecosemente.entity.Semente;

public class ItemVendaDAO {

    private SQLiteDatabase sqLiteDatabase;
    private SementeDAO sementeDAO;

    private ItemVendaDAO(){}

    public ItemVendaDAO(Context context){
        BDEcoSemente bdEcoSemente = new BDEcoSemente(context);
        sqLiteDatabase = bdEcoSemente.getWritableDatabase();
        sementeDAO = new SementeDAO(context);
    }

    public long inserir(ItemVenda item) {
        ContentValues values = new ContentValues();
        values.put("preco_item", item.getPrecoItem());
        values.put("quantidade", item.getQuantidade());
        values.put("semente_id", item.getSemente().getId());
        values.put("venda_id", item.getVendaId());
        return sqLiteDatabase.insert("item_venda", null, values);
    }

    public List<ItemVenda> listarTodos() {
        List<ItemVenda> lista = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("item_venda", null, null, null, null, null, "id ASC");

        while (cursor.moveToNext()) {
            ItemVenda i = new ItemVenda();
            i.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            i.setPrecoItem(cursor.getFloat(cursor.getColumnIndexOrThrow("preco_item")));
            i.setQuantidade(cursor.getInt(cursor.getColumnIndexOrThrow("quantidade")));
            i.setSemente(sementeDAO.getSemente(cursor.getLong(cursor.getColumnIndexOrThrow("semente_id"))));
            i.setVendaId(cursor.getInt(cursor.getColumnIndexOrThrow("venda_id")));
            lista.add(i);
        }
        cursor.close();
        return lista;
    }

    public int atualizar(ItemVenda item) {
        ContentValues values = new ContentValues();
        values.put("preco_item", item.getPrecoItem());
        values.put("quantidade", item.getQuantidade());
        values.put("semente_id", item.getSemente().getId());
        values.put("venda_id", item.getVendaId());
        return sqLiteDatabase.update("item_venda", values, "id = ?", new String[]{String.valueOf(item.getId())});
    }

    public int deletar(long id) {
        return sqLiteDatabase.delete("item_venda", "id = ?", new String[]{String.valueOf(id)});
    }

    public ItemVenda buscarPorId(long id) {
        Cursor cursor = sqLiteDatabase.query("item_venda", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            ItemVenda i = new ItemVenda();
            i.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            i.setPrecoItem(cursor.getFloat(cursor.getColumnIndexOrThrow("preco_item")));
            i.setQuantidade(cursor.getInt(cursor.getColumnIndexOrThrow("quantidade")));
            i.setSemente(sementeDAO.getSemente(cursor.getLong(cursor.getColumnIndexOrThrow("semente_id"))));
            i.setVendaId(cursor.getInt(cursor.getColumnIndexOrThrow("venda_id")));
            cursor.close();
            return i;
        }
        return null;
    }

    public List<ItemVenda> buscarItensPorVendaId(long vendaId) {
        List<ItemVenda> lista = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(
                "item_venda",
                null,
                "venda_id = ?",
                new String[]{String.valueOf(vendaId)},
                null,
                null,
                "id ASC"
        );

        while (cursor.moveToNext()) {
            ItemVenda i = new ItemVenda();
            i.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            i.setPrecoItem(cursor.getFloat(cursor.getColumnIndexOrThrow("preco_item")));
            i.setQuantidade(cursor.getInt(cursor.getColumnIndexOrThrow("quantidade")));
            System.out.println("Semente no item" + cursor.getLong(cursor.getColumnIndexOrThrow("semente_id")));
            i.setSemente(sementeDAO.getSemente(cursor.getLong(cursor.getColumnIndexOrThrow("semente_id"))));
            i.setVendaId(cursor.getInt(cursor.getColumnIndexOrThrow("venda_id")));
            lista.add(i);
        }
        cursor.close();
        return lista;
    }

}
