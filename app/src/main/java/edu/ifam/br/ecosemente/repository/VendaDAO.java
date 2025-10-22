package edu.ifam.br.ecosemente.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.ifam.br.ecosemente.entity.Venda;

public class VendaDAO {

    private SQLiteDatabase sqLiteDatabase;
    private CompradorDAO compradorDAO ;

    private VendaDAO(){}

    public VendaDAO(Context context){
        BDEcoSemente ecoSemente = new BDEcoSemente(context);
        sqLiteDatabase = ecoSemente.getWritableDatabase();
        compradorDAO = new CompradorDAO(context);
    }


    public int inserir(Venda venda) {
        ContentValues values = new ContentValues();
        values.put("data_venda", venda.getDataVenda().toString());
        values.put("valor_total", venda.getValorTotal());
        values.put("comprador_id", venda.getComprador().getId());
        return (int) sqLiteDatabase.insert("venda", null, values);
    }

    public List<Venda> listarTodas() {
        List<Venda> lista = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.query("venda", null, null, null, null, null, "data_venda DESC");

        while (cursor.moveToNext()) {
            Venda v = new Venda();
            v.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            try {
                String dataString = cursor.getString(cursor.getColumnIndexOrThrow("data_venda"));

                // Defina o formato da sua data string (ajuste conforme seu formato)
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Exemplo para formato comum de banco de dados

                // Converta a String para Date
                Date dataVenda = formato.parse(dataString);

                // Atribua ao seu objeto
                v.setDataVenda(dataVenda);

            } catch (ParseException e) {
                e.printStackTrace();
                // Trate o erro conforme necessário - pode definir uma data padrão ou null
                v.setDataVenda(new Date()); // ou new Date() para data atual, por exemplo
            }
            v.setValorTotal(cursor.getFloat(cursor.getColumnIndexOrThrow("valor_total")));
            System.out.println(cursor.getFloat(cursor.getColumnIndexOrThrow("valor_total")));
            System.out.println(cursor.getLong(cursor.getColumnIndexOrThrow("comprador_id")));
            v.setComprador(compradorDAO.buscarPorId(cursor.getLong(cursor.getColumnIndexOrThrow("comprador_id"))));
            System.out.println(v.getComprador());
            lista.add(v);
        }
        cursor.close();
        return lista;
    }

    public int atualizar(Venda venda) {
        ContentValues values = new ContentValues();
        values.put("data_venda", venda.getDataVenda().toString());
        values.put("valor_total", venda.getValorTotal());
        values.put("comprador_id", venda.getComprador().getId());
        return sqLiteDatabase.update("venda", values, "id = ?", new String[]{String.valueOf(venda.getId())});
    }

    public int deletar(long id) {
        return sqLiteDatabase.delete("venda", "id = ?", new String[]{String.valueOf(id)});
    }

    public Venda buscarPorId(long id) {
        Cursor cursor = sqLiteDatabase.query("venda", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            Venda v = new Venda();
            v.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            try {
                String dataString = cursor.getString(cursor.getColumnIndexOrThrow("data_venda"));

                // Defina o formato da sua data string (ajuste conforme seu formato)
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Exemplo para formato comum de banco de dados

                // Converta a String para Date
                Date dataVenda = formato.parse(dataString);

                // Atribua ao seu objeto
                v.setDataVenda(dataVenda);

            } catch (ParseException e) {
                e.printStackTrace();
                // Trate o erro conforme necessário - pode definir uma data padrão ou null
                v.setDataVenda(null); // ou new Date() para data atual, por exemplo
            }
            v.setValorTotal(cursor.getFloat(cursor.getColumnIndexOrThrow("valor_total")));
            v.setComprador(compradorDAO.buscarPorId(cursor.getLong(cursor.getColumnIndexOrThrow("comprador_id"))));
            cursor.close();
            return v;
        }
        return null;
    }
}
